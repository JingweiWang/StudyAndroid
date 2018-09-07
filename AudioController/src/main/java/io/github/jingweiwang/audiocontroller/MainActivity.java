package io.github.jingweiwang.audiocontroller;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_play_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(PlayerFrontActivity.class);
            }
        });

        findViewById(R.id.btn_play_activity_bilibili).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(PlayerFrontActivityByBilibili.class);
            }
        });

        findViewById(R.id.btn_play_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(PlayerBackActitity.class);
            }
        });
    }

    private void jump(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}