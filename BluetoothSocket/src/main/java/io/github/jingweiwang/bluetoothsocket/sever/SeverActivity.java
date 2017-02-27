package io.github.jingweiwang.bluetoothsocket.sever;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.jingweiwang.bluetoothsocket.BluetoothUtils;
import io.github.jingweiwang.bluetoothsocket.R;

/**
 * 受体，进入之后，自动打开蓝牙，并且进入配对模式。
 * 配对后自动开启socket并连接监听
 */
public class SeverActivity extends AppCompatActivity implements BluetoothUtils.SocketCallback {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final int JSON_INDENT = 4;
    @BindView(R.id.tv_socket_title)
    TextView tv_socket_title;
    @BindView(R.id.tv_socket)
    TextView tv_socket;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sever);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        BluetoothUtils.init(this);
        BluetoothUtils.getInstance().setSocketCallback(this);
        printLog("2016©JingweiWang");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothUtils.getInstance().openBluetooth();
                BluetoothUtils.getInstance().startDiscoverable();
                BluetoothUtils.getInstance().pairing();

                BluetoothUtils.getInstance().startServerThread();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        BluetoothUtils.getInstance().unregisterPairingReceiver();
        BluetoothUtils.getInstance().stopDiscoverable();
        BluetoothUtils.getInstance().shutdownServer();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        printLog(msg);
    }

    public void printLog(String msg) {
        String message;
        tv_socket.setText(tv_socket.getText() + new Date().toString());
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        message = LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            tv_socket.setText(tv_socket.getText() + line + "\n");
        }
        tv_socket.setText(tv_socket.getText() + "\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onSocketCallback(int result) {
        switch (result) {
            case BluetoothUtils.STATUS_CONNECTING:
                printLog("正在等待连接...");
                Toast.makeText(this, "STATUS_CONNECTING", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothUtils.STATUS_CONNECTED:
                printLog("已连接, 数据等待中...");
                Toast.makeText(this, "STATUS_CONNECTED", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothUtils.STATUS_CONNECT_ERROR:
                printLog("连接错误, 重新连接...");
                Toast.makeText(this, "STATUS_CONNECT_ERROR", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
