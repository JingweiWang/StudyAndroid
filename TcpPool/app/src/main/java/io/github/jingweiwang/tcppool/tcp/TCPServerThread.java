package io.github.jingweiwang.tcppool.tcp;

import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import io.github.jingweiwang.tcppool.OperationBean;

/**
 * TCP服务端线程 on 16/7/28
 *
 * @author JingweiWang
 */
public class TCPServerThread implements Runnable {
    private Socket socket = null;
    private InputStream inputStream;
    private boolean isRead = true;

    public TCPServerThread(Socket socket) {
        try {
            this.socket = socket;
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            KLog.e("TCPServerThread-->" + e.getMessage());
        }
    }

    @Override
    public void run() {
        String content = "";
        Gson gson = new Gson();
        OperationBean operationBean;
        TCPConnectionBean tcpConnectionBean;

        startHeartBeat();
        while (isRead) {
            if ((content = readFromClient()) != null && !content.equals("")) {
                KLog.json("TCPServerThread-->content", content);
                operationBean = gson.fromJson(content, OperationBean.class);
                if ("HI".equals(operationBean.getContent())) {
                    String from = operationBean.getFrom();
                    KLog.e("TCP建立连接,HI命令接收到了,可以确立归属人为:" + from);
                    TCPConnectionPool.getInstance().addFrom(socket, from);
                    TCPOutput.post(from, "Hi, client.");
                } else {
                    operationBean.setFrom("");//将收到的 operationBean 中 from 清空
                    tcpConnectionBean = TCPConnectionPool.getInstance().getTcpConnectionBean();
                    for (int i = 0; i < tcpConnectionBean.getBodyList().size(); i++) {
                        if (tcpConnectionBean.getBodyList().get(i).getSocket().equals(socket)) {
                            operationBean.setFrom(tcpConnectionBean.getBodyList().get(i).getFrom());
                        }
                    }
                    if (!operationBean.getFrom().equals("")) {
                        KLog.json("TCPServerThread-->operationBean", gson.toJson(operationBean));
                        String string = operationBean.getContent();
                        /**
                         * TODO:可以在这里将收到的 string 通过 EventBus 发送出去
                         */
                    }
                }
            }
        }
    }

    private String readFromClient() {
        ByteArrayOutputStream byteArrayOutputStream;
        byte b;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            while ((b = (byte) inputStream.read()) != -1) {
                byteArrayOutputStream.write(b);
            }
            String s = byteArrayOutputStream.toString();
            byteArrayOutputStream.close();
            return s;
        } catch (IOException e) {
            KLog.e("readFromClient-->" + e.getMessage());
        }
        return null;
    }

    private void startHeartBeat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    do {
                        KLog.w("heart beat");
                        socket.sendUrgentData(97);
                        Thread.sleep(3000);
                    } while (true);
                } catch (IOException | InterruptedException e) {
                    KLog.e("socket is close");
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        KLog.e("startHeartBeat-->" + e.getMessage());
                    }
                    isRead = false;
                    TCPConnectionPool.getInstance().removeSocket(socket);
                    KLog.e("isRead-->" + isRead);
                }
            }
        }).start();
    }
}
