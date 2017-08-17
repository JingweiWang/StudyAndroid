package jingweiwang.github.io.audiocontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_play_activity)
    Button btn_play_activity;
    @BindView(R.id.btn_play_service)
    Button btn_play_service;
    @BindView(R.id.btn_play_activity_bilibili)
    Button btn_play_activity_bilibili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_play_activity)
    public void onBtnPlayActivityClicked() {
        jump(PlayerFrontActivity.class);
    }

    @OnClick(R.id.btn_play_activity_bilibili)
    public void onBtnPlayActivityBilibiliClicked() {
        jump(PlayerFrontActivityByBilibili.class);
    }

    @OnClick(R.id.btn_play_service)
    public void onBtnPlayServiceClicked() {
        jump(PlayerBackActitity.class);
    }

    private void jump(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}