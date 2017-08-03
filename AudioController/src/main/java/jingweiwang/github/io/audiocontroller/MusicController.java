package jingweiwang.github.io.audiocontroller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wangjingwei on 2017/7/19.
 */

public class MusicController {
    private static final String TAG = MusicController.class.getSimpleName();

    private static boolean isMusicFlowServiceRun = false;
    private static Context applicationContext;

    public static void play() {
        EventBus.getDefault().post("play");
    }

    public static void stop() {
        EventBus.getDefault().post("stop");
    }

    public static void startTTS() {
        EventBus.getDefault().post("tts_start");
    }

    public static void endTTS() {
        EventBus.getDefault().post("tts_end");
    }

    public static void init(Application application) {
        if (application != null && applicationContext == null) {
            applicationContext = application.getApplicationContext();
            if (applicationContext == null) {
                applicationContext = application.getApplicationContext();
            }
        }
    }

    public static void startService() {
        if (applicationContext == null) {
            try {
                throw new Exception("请先初始化!");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            if (!isMusicFlowServiceRun) {
                applicationContext.startService(new Intent(applicationContext, MusicService.class));
                isMusicFlowServiceRun = true;
            }
        }
    }

    public static void stopService() {
        if (isMusicFlowServiceRun) {
            applicationContext.stopService(new Intent(applicationContext, MusicService.class));
            isMusicFlowServiceRun = false;
        }
    }
}
