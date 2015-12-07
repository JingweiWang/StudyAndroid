package io.github.jingweiwang.testdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("生命周期", "onCreate");
		Button open = (Button) findViewById(R.id.open);

		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this).setMessage("hello")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("生命周期", "onStart");

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("生命周期", "onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("生命周期", "onPause");

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("生命周期", "onRestart");

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("生命周期", "onStop");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("生命周期", "onDestroy");

	}
}
