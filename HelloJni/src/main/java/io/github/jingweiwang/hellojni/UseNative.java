package io.github.jingweiwang.hellojni;

/**
 * Created by jingweiwang on 2016/11/2.
 */

public class UseNative {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native double multiplication(double x, double y);
}
