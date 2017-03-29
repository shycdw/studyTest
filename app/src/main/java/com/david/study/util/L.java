package com.david.study.util;

import android.util.Log;

/**
 * 日志工具类
 * Created by DavidChen on 2015/12/19.
 */
public class L {

    private L() {
        throw new UnsupportedOperationException("can not be instantiate");
    }

    public static final int VERBOSE = 0;

    public static final int DEBUG = 1;

    public static final int INFO = 2;

    public static final int WARN = 3;

    public static final int ERROR = 4;

    public static final int NOTHING = 5;

    public static int sLevel = VERBOSE;

    public static void v(String tag, String msg) {
        if (sLevel <= VERBOSE) Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (sLevel <= DEBUG) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (sLevel <= INFO) Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (sLevel <= WARN) Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (sLevel <= ERROR) Log.e(tag, msg);
    }
}
