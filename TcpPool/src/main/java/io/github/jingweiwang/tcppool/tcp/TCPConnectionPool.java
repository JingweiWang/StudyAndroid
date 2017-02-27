package io.github.jingweiwang.tcppool.tcp;

import com.google.gson.Gson;
import com.socks.library.KLog;

import java.net.Socket;

/**
 * TCP服务端线程池 on 16/7/28
 *
 * @author JingweiWang
 */
public class TCPConnectionPool {
    private static TCPConnectionPool tcpConnectionPool;
    private Gson gson = new Gson();
    private TCPConnectionBean tcpConnectionBean = new TCPConnectionBean();

    public static TCPConnectionPool getInstance() {
        if (tcpConnectionPool == null) {
            tcpConnectionPool = new TCPConnectionPool();
        }
        return tcpConnectionPool;
    }

    public void initSocket(Socket socket) {
        TCPConnectionBean.BodyList bodyList = new TCPConnectionBean.BodyList();
        bodyList.setSocket(socket);
        bodyList.setFrom("");
        tcpConnectionBean.getBodyList().add(bodyList);
        KLog.json("TCPConnectionPool-->initedSocket", gson.toJson(tcpConnectionBean));
    }

    public void addFrom(Socket socket, String id) {
        for (int i = 0; i < tcpConnectionBean.getBodyList().size(); i++) {
            if (tcpConnectionBean.getBodyList().get(i).getSocket().equals(socket)) {
                tcpConnectionBean.getBodyList().get(i).setFrom(id);
            }
        }
        KLog.json("TCPConnectionPool-->addedFrom", gson.toJson(tcpConnectionBean));
    }

    public void removeSocket(Socket socket) {
        for (int i = tcpConnectionBean.getBodyList().size() - 1; i >= 0; i--) {
            if (tcpConnectionBean.getBodyList().get(i).getSocket().equals(socket)) {
                tcpConnectionBean.getBodyList().remove(i);
            }
        }
        KLog.json("TCPConnectionPool-->removedSocket", gson.toJson(tcpConnectionBean));
    }

    public Socket getSocket(String id) {
        for (int i = 0; i < tcpConnectionBean.getBodyList().size(); i++) {
            if (tcpConnectionBean.getBodyList().get(i).getFrom().equals(id)) {
                KLog.i("TCPConnectionPool-->getSocket", tcpConnectionBean.getBodyList().get(i).getSocket());
                return tcpConnectionBean.getBodyList().get(i).getSocket();
            }
        }
        return null;
    }

    public TCPConnectionBean getTcpConnectionBean() {
        return tcpConnectionBean;
    }
}
