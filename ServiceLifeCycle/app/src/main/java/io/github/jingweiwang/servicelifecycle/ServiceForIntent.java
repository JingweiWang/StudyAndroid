package io.github.jingweiwang.servicelifecycle;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ServiceForIntent extends IntentService {
    private static final String TAG = ServiceForIntent.class.getSimpleName();

    public ServiceForIntent() {
        super("ServiceForIntent");
        Log.e(TAG, "ServiceForIntent in " + Thread.currentThread().getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate in " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand in " + Thread.currentThread().getName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy in " + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent in " + Thread.currentThread().getName());
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.e(TAG, "setIntentRedelivery in " + Thread.currentThread().getName());
    }


}
