package io.github.jingweiwang.wifisample;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    // 右侧滚动条按钮
    private ScrollView sView;
    private TextView allNetWork;
    private Button scan;
    private Button start;
    private Button stop;
    private Button check;
    private WifiAdmin mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer mStringBuffer = new StringBuffer();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWifiAdmin = new WifiAdmin(MainActivity.this);
        init();
    }

    // 按钮的初始化
    public void init() {
        sView = (ScrollView) findViewById(R.id.mScrollView);
        allNetWork = (TextView) findViewById(R.id.allNetWork);
        scan = (Button) findViewById(R.id.scan);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        check = (Button) findViewById(R.id.check);
        scan.setOnClickListener(MainActivity.this);
        start.setOnClickListener(MainActivity.this);
        stop.setOnClickListener(MainActivity.this);
        check.setOnClickListener(MainActivity.this);
    }

    // WIFI_STATE_DISABLING 0
    // WIFI_STATE_DISABLED 1
    // WIFI_STATE_ENABLING 2
    // WIFI_STATE_ENABLED 3
    public void start() {
        mWifiAdmin.openWifi();
        Toast.makeText(MainActivity.this, "当前Wifi网卡状态为" + mWifiAdmin.checkState(),
                Toast.LENGTH_SHORT).show();
    }

    public void stop() {
        mWifiAdmin.closeWifi();
        Toast.makeText(MainActivity.this, "当前Wifi网卡状态为" + mWifiAdmin.checkState(),
                Toast.LENGTH_SHORT).show();
    }

    public void check() {
        Toast.makeText(MainActivity.this, "当前Wifi网卡状态为" + mWifiAdmin.checkState(),
                Toast.LENGTH_SHORT).show();
    }

    public void getAllNetWorkList() {
        // 每次点击扫描之前清空上一次的扫描结果
        if (mStringBuffer != null) {
            mStringBuffer = new StringBuffer();
        }

        // 开始扫描网络
        mWifiAdmin.startScan();
        list = mWifiAdmin.getWifiList();

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mScanResult = list.get(i);
                // 得到网络的SSID：the network name
//                try {
//                    Field f1 = mScanResult.getClass().getDeclaredField("bytes");
//                    f1.setAccessible(true);
//                    byte[] b = f1.get()
//                    mStringBuffer = mStringBuffer
//                            .append(mScanResult.SSID).append("        ")
////                            .append(f1).append("        ")
//                            .append(mScanResult.BSSID).append("        ")
//                            .append(mScanResult.capabilities).append("        ")
//                            .append(mScanResult.frequency).append("        ")
//                            .append(mScanResult.level).append("        ")
//                            .append("\n\n");

                mStringBuffer = mStringBuffer
                        .append(mScanResult.toString())
                        .append("\n\n");
//                mStringBuffer = mScanResult.toString();
//                mStringBuffer = mStringBuffer.append("\n\n");
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
            }
            allNetWork.setText("扫描到的所有Wifi网络：\n" + mStringBuffer.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan:
                getAllNetWorkList();
                break;
            case R.id.start:
                start();
                break;
            case R.id.stop:
                stop();
                break;
            case R.id.check:
                check();
                break;
            default:
                break;
        }
    }
}

