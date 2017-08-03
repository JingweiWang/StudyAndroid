package jingweiwang.github.io.audiocontroller;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerBackActitity extends AppCompatActivity {
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
    Button btn_play_other;
    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_back);
        ButterKnife.bind(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        tv_audio_volume.setText(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
        MusicController.init(this.getApplication());
        MusicController.startService();
    }

    @Override
    protected void onDestroy() {
        MusicController.stopService();
        super.onDestroy();
    }

    @OnClick(R.id.btn_play)
    public void onBtnPlayClicked() {
        MusicController.play();
    }

    @OnClick(R.id.btn_stop)
    public void onBtnStopClicked() {
        MusicController.stop();
    }

    @OnClick(R.id.btn_setVolume_sub)
    public void onBtnSetVolumeSubClicked() {
        MusicController.startTTS();
    }

    @OnClick(R.id.btn_setVolume_add)
    public void onBtnSetVolumeAddClicked() {
        MusicController.endTTS();
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
}
