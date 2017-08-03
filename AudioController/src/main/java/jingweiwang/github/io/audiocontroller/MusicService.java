package jingweiwang.github.io.audiocontroller;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = MusicService.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private boolean eventBusFlag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setEventBus(true);
    }

    @Override
    public void onDestroy() {
        setEventBus(false);
        playbackEnd();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 播放本地音乐
     */
    private void playLocationMusic(int resid) {
        release();
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(resid);
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
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e(TAG, "歌曲准备完毕");
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        Log.e(TAG, "歌曲播放ing...");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e(TAG, "歌曲播放完毕");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    @Subscribe
    public void onEventMainThread(String string) {
        switch (string) {
            case "play":
                playLocationMusic(R.raw.m500003oulho2hcrhc);
                break;
            case "stop":
                playbackEnd();
                break;
            case "tts_start":
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0.1f, 0.1f);
                    Log.e(TAG, "setVolume(0.1f, 0.1f)");
                }
                break;
            case "tts_end":
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    Log.e(TAG, "setVolume(1.0f, 1.0f)");
                }

                break;
            default:
                break;
        }
    }

    private void setEventBus(boolean bool) {
        if (bool) {
            if (!eventBusFlag) {
                EventBus.getDefault().register(this);
                eventBusFlag = true;
            }
        } else {
            if (eventBusFlag) {
                EventBus.getDefault().unregister(this);
                eventBusFlag = false;
            }
        }
    }

    private void playbackEnd() {
        release();
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
