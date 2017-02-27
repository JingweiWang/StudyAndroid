package io.github.jingweiwang.tcppool.tcp;

import com.socks.library.KLog;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * TCP服务端输出 on 16/7/28
 *
 * @author JingweiWang
 */
public class TCPOutput {
    public static void post(final String imuid, final String contect) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;
                Socket socket = TCPConnectionPool.getInstance().getSocket(imuid);
                try {
                    outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    byte[] originBytes = contect.getBytes();
                    byte[] bytes = Arrays.copyOf(originBytes, originBytes.length + 1);
                    bytes[bytes.length - 1] = -1;
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.flush();
                    KLog.json("TCPOutput-->posted-->contect-->", contect);
                } catch (Exception e) {
                    KLog.e("TCPOutput-->" + e.getMessage());
                }
            }
        }).start();
    }
}