package io.github.jingweiwang.audiocontroller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerFrontActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "AudioController";
    @BindView(R.id.btn_volumeAdd)
    Button btn_volumeAdd;
    @BindView(R.id.btn_volumeSubtract)
    Button btn_volumeSubtract;
    @BindView(R.id.btn_autoAdd)
    Button btn_autoAdd;
    @BindView(R.id.btn_autoSubtract)
    Button btn_autoSubtract;
    @BindView(R.id.tv_audio_volume)
    TextView tv_audio_volume;
    @BindView(R.id.btn_play)
    Button btn_play;
    @BindView(R.id.btn_setVolume_sub)
    Button btn_setVolume_sub;
    @BindView(R.id.btn_setVolume_add)
    Button btn_setVolume_add;
    @BindView(R.id.btn_stop)
    Button btn_stop;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_front);
        ButterKnife.bind(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    @OnClick(R.id.btn_volumeAdd)
    public void onBtnVolumeAddClicked() {
        int maxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolum < maxVolum) {
            currentVolum++;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_PLAY_SOUND);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
    }

    @OnClick(R.id.btn_volumeSubtract)
    public void onBtnVolumeSubtractClicked() {
        int currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolum > 0) {
            currentVolum--;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolum, AudioManager.FLAG_PLAY_SOUND);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
    }

    @OnClick(R.id.btn_autoAdd)
    public void onBtnAutoAddClicked() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
    }

    @OnClick(R.id.btn_autoSubtract)
    public void onBtnAutoSubtractClicked() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
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

    @OnClick(R.id.btn_play)
    public void onBtnPlayClicked() {
        playLocationMusic(R.raw.m500003oulho2hcrhc);
    }

    @OnClick(R.id.btn_stop)
    public void onBtnStopClicked() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @OnClick(R.id.btn_setVolume_sub)
    public void onBtnSetVolumeSubClicked() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume(0.1f, 0.1f);
            Log.e(TAG, "mediaPlayer.setVolume(0.1f, 0.1f) 成功");
        } else {
            Log.e(TAG, "mediaPlayer.setVolume(0.1f, 0.1f) 失败, 条件不符");
        }
    }

    @OnClick(R.id.btn_setVolume_add)
    public void onBtnSetVolumeAddClicked() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume(1.0f, 1.0f);
            Log.e(TAG, "mediaPlayer.setVolume(1.0f, 1.0f) 成功");
        } else {
            Log.e(TAG, "mediaPlayer.setVolume(1.0f, 1.0f) 失败, 条件不符");
        }
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