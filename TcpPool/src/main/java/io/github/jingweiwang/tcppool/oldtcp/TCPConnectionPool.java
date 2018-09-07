package io.github.jingweiwang.tcppool.oldtcp;

import androidx.collection.ArrayMap;

import com.socks.library.KLog;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by JingweiWang on 16/7/13.
 * 此类已经作废,请尽快将使用此类的TCP迁移 on 16/7/28.
 *
 * @see io.github.jingweiwang.tcppool.tcp.TCPConnectionPool
 */
@Deprecated
public class TCPConnectionPool {
    private static ArrayList<ArrayMap<String, Object>> socketArrayList = new ArrayList<>();

    public static ArrayList<ArrayMap<String, Object>> getSocketArrayList() {
        return socketArrayList;
    }

    public static void add(Socket socket, String id) {
        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
        arrayMap.put("socket", socket);
        arrayMap.put("from", id);
        socketArrayList.add(arrayMap);
        KLog.i("socketArrayList-->added-->" + socketArrayList);
    }

    public static void update(Socket socket, String id) {
        remove(id);
        add(socket, id);
        KLog.i("socketArrayList-->updated-->" + socketArrayList);
    }


    public static void remove(String id) {
        Iterator<ArrayMap<String, Object>> it = socketArrayList.iterator();
        while (it.hasNext()) {
            ArrayMap<String, Object> am = it.next();
            if (id.equals(am.get("from"))) {
                it.remove();
            }
        }
        KLog.i("socketArrayList-->removed-->" + socketArrayList);
    }

    public static Socket getSocket(String id) {
        Iterator<ArrayMap<String, Object>> it = socketArrayList.iterator();
        while (it.hasNext()) {
            ArrayMap<String, Object> am = it.next();
            if (id.equals(am.get("from"))) {
                return (Socket) am.get("socket");
            }
        }
        return null;
    }
}
