package io.github.jingweiwang.bluetoothsocket;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothUtils {
    public static final int STATUS_CONNECTING = 0x10;
    public static final int STATUS_CONNECTED = 0x11;
    public static final int STATUS_CONNECT_ERROR = 0x12;
    private static final int READ_CONTENT = 0x20;
    private static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static BluetoothUtils instance;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private ServerThread serverThread;
    private ClientThread clientThread;
    private ReadThread readThread;
    private SocketCallback socketCallback;
    private boolean isSingleton = true;
    private boolean ispairingReceiver = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            switch (msg.what) {
                case STATUS_CONNECTING:
                    if (socketCallback != null)
                        socketCallback.onSocketCallback(STATUS_CONNECTING);
                    break;
                case STATUS_CONNECTED:
                    if (socketCallback != null)
                        socketCallback.onSocketCallback(STATUS_CONNECTED);
                    break;
                case STATUS_CONNECT_ERROR:
                    if (socketCallback != null)
                        socketCallback.onSocketCallback(STATUS_CONNECT_ERROR);
                    break;
                case READ_CONTENT:
                    EventBus.getDefault().post(info);
                    break;
                default:
                    break;
            }
        }
    };
    private BroadcastReceiver foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("onReceive action", action);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    //找到设备
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress())) {
                        Log.e("find device", device.getName() + " " + device.getAddress() + " " + device.getBluetoothClass().getDeviceClass());
                        if (!bluetoothDeviceList.contains(device)) {
                            bluetoothDeviceList.add(device);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private BroadcastReceiver pairingReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("onReceive action", action);
            switch (action) {
                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e("onReceive btDevice", btDevice.getName() + " " + btDevice.getAddress());
                    setPairingConfirmation(btDevice.getClass(), btDevice, true);
                    abortBroadcast();
                    break;
                default:
                    break;
            }
        }
    };

    private BluetoothUtils(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new BluetoothUtils(context);
        }
    }

    public static BluetoothUtils getInstance() {
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setPairingConfirmation(Class<?> btClass, BluetoothDevice device, boolean isConfirm) {
        Method setPairingConfirmation = null;
        try {
            setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", boolean.class);
            setPairingConfirmation.invoke(device, isConfirm);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void openBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();//强制开启蓝牙
            Log.e("openBluetooth", "Bluetooth Turned on");
        } else {
            Log.e("openBluetooth", "Bluetooth Already on");
        }
    }

    public void discovery() {
        stopDiscovery();
        startDiscovery();
    }

    /**
     * 需要申请ACCESS_COARSE_LOCATION权限
     */
    private void startDiscovery() {
        if (isSingleton) {
            isSingleton = false;
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0x887);
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(context, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }
            }
            IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(foundReceiver, foundFilter);
            bluetoothAdapter.startDiscovery();
        }
    }

    public void stopDiscovery() {
        if (!isSingleton) {
            bluetoothAdapter.cancelDiscovery();
            context.unregisterReceiver(foundReceiver);
            isSingleton = true;
        }
    }

    /**
     * 开启蓝牙发现模式
     */
    public void startDiscoverable() {
        while (true) {
            if (bluetoothAdapter.isEnabled()) {
                try {
                    Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
                    setDiscoverableTimeout.setAccessible(true);
                    Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
                    setScanMode.setAccessible(true);

                    setDiscoverableTimeout.invoke(bluetoothAdapter, Integer.MAX_VALUE);
                    setScanMode.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, Integer.MAX_VALUE);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 关闭蓝牙发现模式
     */
    public void stopDiscoverable() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, 1);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void pairing() {
        if (!ispairingReceiver) {
            IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
            context.registerReceiver(pairingReceiver, foundFilter);
            ispairingReceiver = true;
        }
    }

    public void unregisterPairingReceiver() {
        if (ispairingReceiver) {
            context.unregisterReceiver(pairingReceiver);
            ispairingReceiver = false;
        }
    }

    public void startServerThread() {
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void startClientThread(BluetoothDevice bluetoothDevice) {
        clientThread = new ClientThread(bluetoothDevice);
        clientThread.start();
    }

    // 发送数据
    public void sendMessageHandle(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (socket == null) {
                Toast.makeText(context, "没有连接", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                OutputStream os = socket.getOutputStream();
                os.write(msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 停止客户端连接
     */
    public void shutdownClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (clientThread != null) {
                    clientThread.interrupt();
                    clientThread = null;
                }
                if (readThread != null) {
                    readThread.interrupt();
                    readThread = null;
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }).start();
    }

    /**
     * 停止服务器连接
     */
    public void shutdownServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (serverThread != null) {
                    serverThread.interrupt();
                    serverThread = null;
                }
                if (readThread != null) {
                    readThread.interrupt();
                    readThread = null;
                }
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                    if (serverSocket != null) {
                        serverSocket.close();
                        serverSocket = null;
                    }
                } catch (IOException e) {
                    Log.e("shutdownServer", e.getMessage());
                }
            }
        }).start();
    }

    public List<BluetoothDevice> getBluetoothDeviceList() {
        return bluetoothDeviceList;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setSocketCallback(SocketCallback socketCallback) {
        this.socketCallback = socketCallback;
    }

    public interface SocketCallback {
        void onSocketCallback(int result);
    }

    // 客户端线程
    private class ClientThread extends Thread {
        BluetoothDevice bluetoothDevice;

        public ClientThread(BluetoothDevice bluetoothDevice) {
            this.bluetoothDevice = bluetoothDevice;
        }

        @Override
        public void run() {
            try {
                socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                Message msg = new Message();
                msg.what = STATUS_CONNECTING;
                handler.sendMessage(msg);
                socket.connect();
                msg = new Message();
                msg.what = STATUS_CONNECTED;
                handler.sendMessage(msg);
                // 启动接受数据
                readThread = new ReadThread();
                readThread.start();
            } catch (IOException e) {
                Message msg = new Message();
                msg.what = STATUS_CONNECT_ERROR;
                handler.sendMessage(msg);
            }
        }
    }

    // 开启服务器
    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                // 创建一个蓝牙服务器 参数分别：服务器名称、UUID
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                Message msg = new Message();
                msg.what = STATUS_CONNECTING;
                handler.sendMessage(msg);
                /**
                 *  接受客户端的连接请求
                 */
                socket = serverSocket.accept();
                msg = new Message();
                msg.what = STATUS_CONNECTED;
                handler.sendMessage(msg);
                // 启动接受数据
                readThread = new ReadThread();
                readThread.start();
            } catch (IOException e) {
                Message msg = new Message();
                msg.what = STATUS_CONNECT_ERROR;
                handler.sendMessage(msg);
            }
        }
    }

    // 读取数据
    private class ReadThread extends Thread {
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {
                is = socket.getInputStream();
                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Log.e("msg", s);
                        Message msg = new Message();
                        msg.obj = s;
                        msg.what = READ_CONTENT;
                        handler.sendMessage(msg);
                    }
                }
            } catch (IOException e1) {
                if (serverThread != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            shutdownServer();
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startServerThread();
                        }
                    }).start();
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
