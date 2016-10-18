package io.github.jingweiwang.tcppool.oldtcp;

import com.socks.library.KLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by JingweiWang on 16/7/13.
 * 此类已经作废,请尽快将使用此类的TCP迁移 on 16/7/28.
 *
 * @see io.github.jingweiwang.tcppool.tcp.TCPServer
 */
@Deprecated
public class TCPServer {
    private static final String TAG = "OldTCPServer";
    private boolean isAccept = true;

    public void startBoxTCP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KLog.e(TAG, "Start TCP Server");
                    ServerSocket serverSocket = new ServerSocket(60000);
                    while (isAccept) {
                        Socket socket = serverSocket.accept();
                        KLog.e(TAG, "TCPServer 有新的连击接入");
                        KLog.e(TAG, "ip:" + socket.getInetAddress().toString());
                        new Thread(new TCPServerThread(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}