#include <jni.h>
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL
        Java_io_github_jingweiwang_hellojni_UseNative_stringFromJNI(JNIEnv *env, jobject obj);
JNIEXPORT jdouble JNICALL
        Java_io_github_jingweiwang_hellojni_UseNative_multiplication(JNIEnv *env, jobject obj,
                                                                     jdouble x, jdouble y);
}

JNIEXPORT jstring JNICALL
Java_io_github_jingweiwang_hellojni_UseNative_stringFromJNI(JNIEnv *env, jobject obj) {
    std::string hello = "Hello JingweiWang.";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jdouble JNICALL
Java_io_github_jingweiwang_hellojni_UseNative_multiplication(JNIEnv *env, jobject obj, jdouble x,
                                                             jdouble y) {
    return x * y;
}