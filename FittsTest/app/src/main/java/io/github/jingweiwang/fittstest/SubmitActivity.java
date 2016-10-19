package io.github.jingweiwang.fittstest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;

public class SubmitActivity extends AppCompatActivity {
    private long mExitTime;
    private Button button_submit;
    private ProgressBar progressBar_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        button_submit = (Button) findViewById(R.id.button_submit);
        progressBar_submit = (ProgressBar) findViewById(R.id.progressBar_submit);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Environment.getDataDirectory() + "/data/io.github.jingweiwang.fittstest/files/" + MainActivity.filePath);
                try {
                    AVFile file = AVFile.withAbsoluteLocalPath(MainActivity.filePath, Environment.getDataDirectory() + "/data/io.github.jingweiwang.fittstest/files/" + MainActivity.filePath);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                //上传失败
                                Toast.makeText(SubmitActivity.this, "上传失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
                            } else {
                                //上传成功
                                Toast.makeText(SubmitActivity.this, "上传成功,感谢您.", Toast.LENGTH_SHORT).show();
                                button_submit.setEnabled(false);
                                button_submit.setTextColor(getResources().getColor(R.color.Grey));
                            }
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer percentDone) {
                            //打印进度
                            System.out.println("uploading: " + percentDone);
                            progressBar_submit.setProgress(percentDone);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次返回到主页!", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(SubmitActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
