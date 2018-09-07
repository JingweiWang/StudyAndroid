package io.github.jingweiwang.fittstest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Test2Activity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private TextView textView_space, textView_dist, textView_ID, textView_d;
    private RadioButton radioButton_pink, radioButton_blue;
    private long currentTime1, currentTime2, mt;
    private int status = 0, count = 0;//status大组次数,count小组次数
    private int textView_space_width = 350;
    private int n = 4;//测试n个距离
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView_space = (TextView) findViewById(R.id.textView_space);
        textView_dist = (TextView) findViewById(R.id.textView_dist);
        textView_d = (TextView) findViewById(R.id.textView_d);
        textView_ID = (TextView) findViewById(R.id.textView_ID);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton_pink = (RadioButton) findViewById(R.id.radioButton_pink);
        radioButton_blue = (RadioButton) findViewById(R.id.radioButton_blue);

        textView_space.setWidth(textView_space_width);

        AlertDialog.Builder builder = new AlertDialog.Builder(Test2Activity.this)
                .setTitle("感谢!").setMessage("感谢您完成第 1 组试验,请继续.");
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_blue:
                        Log.d("您点击了", "蓝色按钮");
                        currentTime1 = System.currentTimeMillis();
                        try {
                            FileOutputStream fileOutputStream = openFileOutput(MainActivity.filePath, MODE_APPEND);
                            PrintStream printStream = new PrintStream(fileOutputStream);
                            if (currentTime2 != 0) {
                                mt = currentTime1 - currentTime2;
                                printStream.println("距离 " + textView_space_width + " ,直径 " + radioButton_pink.getWidth() + " ,第 " + count + " 次试验: " + mt);
                                System.out.println("距离" + textView_space_width + ",直径" + radioButton_pink.getWidth() + ",第" + count + "次试验,总第" + status + "次试验:" + mt);
                                currentTime2 = 0;
                            }
                            printStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton_pink:
                        Log.d("您点击了", "粉色按钮");
                        currentTime2 = System.currentTimeMillis();
                        try {
                            FileOutputStream fileOutputStream = openFileOutput(MainActivity.filePath, MODE_APPEND);
                            PrintStream printStream = new PrintStream(fileOutputStream);
                            if (currentTime1 != 0) {
                                mt = currentTime2 - currentTime1;
                                printStream.println("距离 " + textView_space_width + " ,直径 " + radioButton_pink.getWidth() + " ,第 " + count + " 次试验: " + mt);
                                System.out.println("距离" + textView_space_width + ",直径" + radioButton_pink.getWidth() + ",第" + count + "次试验,总第" + status + "次试验:" + mt);
                                currentTime1 = 0;
                            }
                            printStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                status++;
                count++;
                progressBar.setProgress(status);
                if (status >= 30 * n + 1) {
                    radioButton_blue.setEnabled(false);
                    radioButton_pink.setEnabled(false);
                    Intent intent = new Intent(Test2Activity.this, Test3Activity.class);
                    startActivity(intent);
                    finish();
                } else if (count % 31 == 0) {
                    textView_space_width += 100;
                    count = 0;
                    status--;
                    currentTime1 = 0;
                    currentTime2 = 0;
                    textView_space.setWidth(textView_space_width);
                }
                textView_dist.setText(textView_space_width + "");
                textView_ID.setText(Math.log((double) (textView_space_width) / radioButton_pink.getWidth() + 1) / Math.log((double) 2) + "");
            }
        });

    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次返回到主页!", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Test2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
