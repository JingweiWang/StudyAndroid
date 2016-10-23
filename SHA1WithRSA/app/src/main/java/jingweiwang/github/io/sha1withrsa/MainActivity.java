package jingweiwang.github.io.sha1withrsa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText et_input;
    Button btn_sign;
    TextView tv_signed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        tv_signed = (TextView) findViewById(R.id.tv_signed);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_input.getText().toString();
                Log.e("str", str);
                String strSigned = SHA1WithRSA.getBase64(getApplicationContext(), str);
                tv_signed.setText(strSigned);
            }
        });
    }
}
