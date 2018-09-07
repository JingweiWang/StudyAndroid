package io.github.jingweiwang.audiocontroller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class PlayerFrontActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayerFrontActivity";
    TextView tv_audio_volume;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_front);
        tv_audio_volume = findViewById(R.id.tv_audio_volume);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");

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

        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLocationMusic(R.raw.m500003oulho2hcrhc);
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
            }
        });

        findViewById(R.id.btn_setVolume_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.1f, 0.1f);
                    Log.e(TAG, "mediaPlayer.setVolume(0.1f, 0.1f) 成功");
                } else {
                    Log.e(TAG, "mediaPlayer.setVolume(0.1f, 0.1f) 失败, 条件不符");
                }
            }
        });

        findViewById(R.id.btn_setVolume_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    Log.e(TAG, "mediaPlayer.setVolume(1.0f, 1.0f) 成功");
                } else {
                    Log.e(TAG, "mediaPlayer.setVolume(1.0f, 1.0f) 失败, 条件不符");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    private void playLocationMusic(int resid) {
        release();
        AssetFileDescriptor afd = getResources().openRawResourceFd(resid);
        if (afd == null) return;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioSessionId(0);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "歌曲准备完毕");
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        Log.e(TAG, "歌曲播放ing...");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "歌曲播放完毕");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.e(TAG, "mediaPlayer释放完毕");
        }
    }
}