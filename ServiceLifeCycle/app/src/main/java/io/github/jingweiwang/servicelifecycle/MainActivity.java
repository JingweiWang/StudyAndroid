package io.github.jingweiwang.servicelifecycle;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    Button start_service_start, stop_service_start, bind_service, unbind_service, start_ServiceIntent;
    private ServiceForBind.InnerBinder myBinder;
    private boolean isBind = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG, "onServiceConnected in " + Thread.currentThread().getName());
            myBinder = (ServiceForBind.InnerBinder) iBinder;
            myBinder.doSomething();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected in " + Thread.currentThread().getName());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_service_start = (Button) findViewById(R.id.start_service_start);
        stop_service_start = (Button) findViewById(R.id.stop_service_start);
        bind_service = (Button) findViewById(R.id.bind_service);
        unbind_service = (Button) findViewById(R.id.unbind_service);
        start_ServiceIntent = (Button) findViewById(R.id.start_ServiceIntent);

        start_service_start.setOnClickListener(this);
        stop_service_start.setOnClickListener(this);
        bind_service.setOnClickListener(this);
        unbind_service.setOnClickListener(this);
        start_ServiceIntent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service_start:
                Intent startIntent = new Intent(this, ServiceForStart.class);
                startService(startIntent);
                break;
            case R.id.stop_service_start:
                Intent stopIntent = new Intent(this, ServiceForStart.class);
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, ServiceForBind.class);
                isBind = bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                if (isBind) {
                    unbindService(connection);
                    isBind = false;
                }
                break;
            case R.id.start_ServiceIntent:
                Intent startServiceIntent = new Intent(this, ServiceForIntent.class);
                startService(startServiceIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(connection);
            isBind = false;
        }
    }
}
