package io.github.jingweiwang.headsetstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isHeadsetOn = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    isHeadsetOn = false;
                    Toast.makeText(context, "耳机被拔出", Toast.LENGTH_SHORT).show();
                } else if (intent.getIntExtra("state", 0) == 1) {
                    isHeadsetOn = true;
                    Toast.makeText(context, "耳机已插入", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AudioManager audioManager = (AudioManager) getApplication().getSystemService(Context.AUDIO_SERVICE);
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 20; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("isHeadsetOn", isHeadsetOn + "");
                }
                destory();
            }
        }).start();
    }

    private void init() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        filter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    private void destory() {
        this.unregisterReceiver(mBroadcastReceiver);
    }
}
