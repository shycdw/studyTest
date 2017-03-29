package com.david.study.util;

/**
 * Created by DavidChen on 2017/1/4.
 */
public class BsPatch {
    static {
        System.loadLibrary("bsdiff");
    }

    public static native int bspatch(String oldApk, String newApk, String patch);
}
