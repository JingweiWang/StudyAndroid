package io.github.jingweiwang.bluetoothsocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.jingweiwang.bluetoothsocket.client.ClientActivity;
import io.github.jingweiwang.bluetoothsocket.sever.SeverActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sever)
    public void onClickBtnSever() {
        Intent intent = new Intent(MainActivity.this, SeverActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_client)
    public void onClickBtnClient() {
        Intent intent = new Intent(MainActivity.this, ClientActivity.class);
        startActivity(intent);
        finish();
    }
}
