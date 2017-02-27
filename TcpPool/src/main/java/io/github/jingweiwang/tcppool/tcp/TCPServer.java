package io.github.jingweiwang.tcppool.tcp;

import com.socks.library.KLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP服务端 on 16/7/28
 *
 * @author JingweiWang
 */
public class TCPServer {
    public static final int TCP_PORT = 60000;
    private static final String TAG = "TCPServer";
    private static TCPServer tcpServer;
    private boolean isBoxTCPStrted = false;
    private boolean isAccept = true;

    public static TCPServer getInstance() {
        if (tcpServer == null) {
            tcpServer = new TCPServer();
        }
        return tcpServer;
    }

    public void startBoxTCP() {
        if (!isBoxTCPStrted) {
            isBoxTCPStrted = true;//app内全局单例使用此方法
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        KLog.e(TAG, "Start TCPServer!");
                        ServerSocket serverSocket = new ServerSocket(TCP_PORT);
                        while (isAccept) {
                            Socket socket = serverSocket.accept();
                            KLog.e(TAG, "TCPServer有新的连击接入,ip:" + socket.getInetAddress().toString());
                            TCPConnectionPool.getInstance().initSocket(socket);//将新连接的socket放入连接池
                            new Thread(new TCPServerThread(socket)).start();
                        }
                    } catch (IOException e) {
                        KLog.e("TCPServer-->startBoxTCP-->" + e.getMessage());
                    } finally {
                        isBoxTCPStrted = false;
                    }
                }
            }).start();
        } else {
            KLog.e(TAG, "TCPServer is already started!");
        }
    }
}
