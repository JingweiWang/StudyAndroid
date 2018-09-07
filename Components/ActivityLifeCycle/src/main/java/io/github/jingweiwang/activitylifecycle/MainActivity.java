package io.github.jingweiwang.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainAct";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        editText = findViewById(R.id.editText);

        findViewById(R.id.btn_openAlertDialog).setOnClickListener(v -> new AlertDialog
                .Builder(MainActivity.this)
                .setMessage("这是一个AlertDialog。")
                .setPositiveButton("确定", (dialog, which) -> Log.e(TAG + "-AlertDialog", "onClick-PositiveButton"))
                .setNegativeButton("取消", (dialog, which) -> Log.e(TAG + "-AlertDialog", "onClick-NegativeButton"))
                .setOnCancelListener(dialog -> Log.e(TAG + "-AlertDialog", "onCancel"))
                .setOnDismissListener(dialog -> Log.e(TAG + "-AlertDialog", "onDismiss"))
                .show()
        );

        findViewById(R.id.btn_openSceondActivity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SceondActivity.class);
            startActivity(intent);
        });

        if (savedInstanceState != null) {
            String extra_edit = savedInstanceState.getString("extra_edit");
            Log.e(TAG, "[onCreate]restore extra_edit: " + extra_edit);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "[onSaveInstanceState]");
        outState.putString("extra_edit", editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String extra_edit = savedInstanceState.getString("extra_edit");
        Log.e(TAG, "[onRestoreInstanceState]restore extra_edit: " + extra_edit);
    }
}
