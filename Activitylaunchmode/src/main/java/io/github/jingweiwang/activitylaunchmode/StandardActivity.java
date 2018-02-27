package io.github.jingweiwang.activitylaunchmode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class StandardActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        textView = findViewById(R.id.textView);
        textView.setText("Activity为: " + this.toString() + "\nTaskID为: " + this.getTaskId());
    }

    public void openMyself(View view) {
        Intent intent = new Intent(StandardActivity.this, StandardActivity.class);
        startActivity(intent);
    }
}
