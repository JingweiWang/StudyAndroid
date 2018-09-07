package io.github.jingweiwang.audiocontroller;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class PlayerBackActitity extends AppCompatActivity {
    TextView tv_audio_volume;
    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_back);
        tv_audio_volume = findViewById(R.id.tv_audio_volume);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
        MusicController.init(this.getApplication());
        MusicController.startService();

        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.play();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.stop();
            }
        });

        findViewById(R.id.btn_setVolume_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.startTTS();
            }
        });

        findViewById(R.id.btn_setVolume_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.endTTS();
            }
        });

        findViewById(R.id.btn_volumeAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolum < maxVolum) {
                    currentVolum++;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_PLAY_SOUND);
                tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
            }
        });

        findViewById(R.id.btn_volumeSubtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolum > 0) {
                    currentVolum--;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_PLAY_SOUND);
                tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
            }
        });

        findViewById(R.id.btn_autoAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
            }
        });
        
        findViewById(R.id.btn_autoSubtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
            }
        });
    }

    @Override
    protected void onDestroy() {
        MusicController.stopService();
        super.onDestroy();
    }
}
