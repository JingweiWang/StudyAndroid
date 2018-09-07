package io.github.jingweiwang.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public static final String ACTION = "io.github.jingweiwang.broadcast.intent.action.MyReceiver";
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "接收到的消息为：" + intent.getStringExtra("data"));
    }
}
