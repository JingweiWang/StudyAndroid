package io.github.jingweiwang.tcppool.oldtcp;

import com.socks.library.KLog;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by JingweiWang on 16/7/14.
 * 此类已经作废,请尽快将使用此类的TCP迁移 on 16/7/28.
 *
 * @see io.github.jingweiwang.tcppool.tcp.TCPOutput
 */
@Deprecated
public class TCPOutput {
    public static void output(final String id, final String contect) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;
                Socket socket = TCPConnectionPool.getSocket(id);
                KLog.i("TCPOutput-->socket-->" + socket);
                KLog.i("TCPOutput-->contect-->" + contect);
                try {
                    outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    byte[] originBytes = contect.getBytes();
                    byte[] bytes = Arrays.copyOf(originBytes, originBytes.length + 1);
                    bytes[bytes.length - 1] = -1;
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.flush();
                } catch (Exception e) {
                    KLog.e("TCPOutput-->" + e.getMessage());
                    TCPConnectionPool.remove(id);
                }
            }
        }).start();
    }
}
