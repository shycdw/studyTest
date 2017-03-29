package com.david.study;

/**
 * Created by DavidChen on 2017/1/5.
 */
public class JNITest {

    static {
        System.loadLibrary("JNITest");
    }

    public static native String printJNI();
}
