package io.github.jingweiwang.sha1withrsa;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String ASSETS_PATH_FOR_PRIAVTE_KEY = "key/private_key_pkcs8.pem";
    private static final String ASSETS_PATH_FOR_PUBLIC_KRY = "key/public_key.pem";
    private EditText et_input;
    private Button btn_sign, btn_verify;
    private TextView tv_signed, tv_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.et_input);
        btn_sign = findViewById(R.id.btn_sign);
        btn_verify = findViewById(R.id.btn_verify);
        tv_signed = findViewById(R.id.tv_signed);
        tv_verified = findViewById(R.id.tv_verified);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_input.getText().toString();
                Log.e("str", str);
                String strSigned = SHA1WithRSA.signWithBase64(getApplicationContext(), ASSETS_PATH_FOR_PRIAVTE_KEY, str);
                tv_signed.setText(strSigned);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_input.getText().toString();
                Log.e("str", str);
                String signed = tv_signed.getText().toString();
                Log.e("signed", signed);
                boolean result = SHA1WithRSA.verify(getApplicationContext(), ASSETS_PATH_FOR_PUBLIC_KRY, str, signed);
                if (result) {
                    tv_verified.setText("验证成功");
                } else {
                    tv_verified.setText("验证失败");
                }
            }
        });
    }
}
