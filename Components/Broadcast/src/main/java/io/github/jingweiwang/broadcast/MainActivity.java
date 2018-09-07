package io.github.jingweiwang.broadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MyReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_send_broadcast).setOnClickListener(v -> {
            Intent i = new Intent(MyReceiver.ACTION);
            i.putExtra("data", "这是一条广播, 发送于 "
                    + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
                    .format(new Date(System.currentTimeMillis())));
            sendBroadcast(i);
        });

        findViewById(R.id.btn_register_receiver).setOnClickListener(v -> {
            if (receiver == null) {
                Log.e(TAG, "Register Receiver");
                receiver = new MyReceiver();
                registerReceiver(receiver, new IntentFilter(MyReceiver.ACTION));
            }
        });

        findViewById(R.id.btn_unregister_receiver).setOnClickListener(v -> {
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
                Log.e(TAG, "Unregister Receiver");
            }
        });
    }
}
