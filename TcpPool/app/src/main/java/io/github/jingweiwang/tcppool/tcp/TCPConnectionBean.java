package io.github.jingweiwang.tcppool.tcp;

import com.google.gson.annotations.SerializedName;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP服务端线程池Bean on 16/7/28
 *
 * @author JingweiWang
 */
public class TCPConnectionBean {
    @SerializedName("body")
    private List<BodyList> bodyList = new ArrayList<>();

    public List<BodyList> getBodyList() {
        return bodyList;
    }

    public static class BodyList {
        @SerializedName("socket")
        private Socket socket;

        @SerializedName("from")
        private String from;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

    }
}
