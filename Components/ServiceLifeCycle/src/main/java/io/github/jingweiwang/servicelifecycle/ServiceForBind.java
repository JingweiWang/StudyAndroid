package io.github.jingweiwang.servicelifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceForBind extends Service {
    private static final String TAG = "ServiceForBind";
    InnerBinder binder = new InnerBinder();

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
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind in " + Thread.currentThread().getName());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind in " + Thread.currentThread().getName());
        return super.onUnbind(intent);
    }

    class InnerBinder extends Binder {
        void doSomething() {
            Log.e(TAG, "doSomething() in " + Thread.currentThread().getName());
        }
    }
}
