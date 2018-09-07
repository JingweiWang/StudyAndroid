package io.github.jingweiwang.activitylaunchmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testStandard(View view) {
        Intent intent = new Intent(MainActivity.this, StandardActivity.class);
        startActivity(intent);
    }

    public void testSingleTop(View view) {
        Intent intent = new Intent(MainActivity.this, SingleTopActivity.class);
        startActivity(intent);
    }

    public void testSingleTask(View view) {
        Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
        startActivity(intent);
    }

    public void testSingleInstance(View view) {
        Intent intent = new Intent(MainActivity.this, SingleInstanceActivity.class);
        startActivity(intent);
    }
}
