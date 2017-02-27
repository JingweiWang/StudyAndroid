package jingweiwang.github.io.audiocontroller;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_volumeAdd, btn_volumeSubtract;
    private Button btn_autoAdd, btn_autoSubtract;

    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_volumeAdd = (Button) findViewById(R.id.btn_volumeAdd);
        btn_volumeSubtract = (Button) findViewById(R.id.btn_volumeSubtract);
        btn_autoAdd = (Button) findViewById(R.id.btn_autoAdd);
        btn_autoSubtract = (Button) findViewById(R.id.btn_autoSubtract);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        btn_volumeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolum < maxVolum) {
                    currentVolum++;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_SHOW_UI);
            }
        });
        btn_volumeSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolum > 0) {
                    currentVolum--;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_SHOW_UI);
            }
        });
        btn_autoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });
        btn_autoSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

            }
        });
    }
}