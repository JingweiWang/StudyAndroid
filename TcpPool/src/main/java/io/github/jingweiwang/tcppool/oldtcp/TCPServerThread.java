package io.github.jingweiwang.tcppool.oldtcp;

import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Iterator;

import io.github.jingweiwang.tcppool.OperationBean;

/**
 * Created by JingweiWang on 16/7/14.
 * 此类已经作废,请尽快将使用此类的TCP迁移 on 16/7/28.
 *
 * @see io.github.jingweiwang.tcppool.tcp.TCPServerThread
 */
@Deprecated
public class TCPServerThread implements Runnable {
    private Socket socket = null;
    private InputStream inputStream;
    private Gson gson = new Gson();
    private OperationBean operationBean;
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
        String content = null;
        startHeartBeat();
        while (isRead) {
            if ((content = readFromClient()) != null && !content.equals("")) {
                KLog.w("content:" + content);
                operationBean = gson.fromJson(content, OperationBean.class);
                String string = operationBean.getContent();
                /**
                 * TODO:可以在这里将收到的 string 通过 EventBus 发送出去
                 */
                boolean isExist = false;
                KLog.i("socketArrayList-->ori-->" + TCPConnectionPool.getSocketArrayList());
                Iterator<ArrayMap<String, Object>> it = TCPConnectionPool.getSocketArrayList().iterator();
                while (it.hasNext()) {
                    ArrayMap<String, Object> am = it.next();
                    if (operationBean.getFrom().equals(am.get("from")) && socket.equals(am.get("socket"))) {
                        isExist = true;
                        //如果 id 和 socket 存在并且不变则空操作
                    } else if (operationBean.getFrom().equals(am.get("from")) && !socket.equals(am.get("socket"))) {
                        isExist = true;
                        //如果 id 存在 socket 有变化则更新(删除再加)
                        TCPConnectionPool.update(socket, operationBean.getFrom());
                    }
                }
                if (!isExist) {
                    //如果都不存在则直接加
                    TCPConnectionPool.add(socket, operationBean.getFrom());
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
                    KLog.e("startHeartBeat-->" + e.getMessage());
                    KLog.e("socket is close");
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        KLog.e("startHeartBeat-->" + e.getMessage());
                    }
                    isRead = false;
                    TCPConnectionPool.remove(operationBean.getFrom());
                    KLog.e("isRead-->" + isRead);
                }
            }
        }).start();
    }
}
