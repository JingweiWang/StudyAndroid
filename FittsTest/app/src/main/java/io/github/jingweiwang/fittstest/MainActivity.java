package io.github.jingweiwang.fittstest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;

public class MainActivity extends AppCompatActivity {

    final public static String filePath = "FittsTest" + (int) (Math.random() * 100000) + ".txt";
    private Button Button_pink, Button_blue, Button_about;
    private Boolean flag_pink = false, flag_blue = false;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);

        AVOSCloud.initialize(this, "vqeYAWugT0l95f0KXeWfTtWB", "UL6REk6NJiBiWgdx8yYJubyJ");

        ButtonListener buttonListener = new ButtonListener();
        Button_pink = (Button) findViewById(R.id.Button_pink);
        Button_blue = (Button) findViewById(R.id.Button_blue);

        Button_pink.setOnTouchListener(buttonListener);
        Button_blue.setOnTouchListener(buttonListener);

        Button_about = (Button) findViewById(R.id.Button_about);
        Button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序!", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.out.println(android.os.Process.myPid());
                android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ButtonListener implements View.OnClickListener, View.OnTouchListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.Button_pink) {
                Log.d("test", "Button_pink button ---> click");
            } else if (v.getId() == R.id.Button_blue) {
                Log.d("test", "Button_blue button ---> click");
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.Button_pink) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (true == flag_blue) {
                        Intent intent = new Intent(MainActivity.this, Test1Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    flag_pink = false;
                    Log.d("test", "Button_pink button ---> cancel");
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    flag_pink = true;
                    Log.d("test", "Button_pink button ---> down");
                }
            } else if (v.getId() == R.id.Button_blue) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (true == flag_pink) {
                        Intent intent = new Intent(MainActivity.this, Test1Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    flag_blue = false;
                    Log.d("test", "Button_blue button ---> cancel");
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    flag_blue = true;
                    Log.d("test", "Button_blue button ---> down");
                }
            }
            return true;
        }
    }

}