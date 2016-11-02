package io.github.jingweiwang.hellojni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.sample_text);

        tv.setText(UseNative.stringFromJNI());
        Log.e("multiplication", "6.8 * 8.9 = " + UseNative.multiplication(6.8, 8.9));
    }
}
