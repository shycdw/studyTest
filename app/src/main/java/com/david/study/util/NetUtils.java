package com.david.study.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接
 * Created by DavidChen on 2015/12/1.
 */
public class NetUtils {
    private NetUtils() {
        throw new UnsupportedOperationException("can not be isntantiated");
    }

    /**
     * 判断网络是否连接
     * @param context context
     * @return true,连接，false，未连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                if (NetworkInfo.State.CONNECTED == networkInfo.getState())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否连接WIFI
     * @param context context
     * @return true，连接WIFI，false，未连接WIFI
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return null != connectivityManager && ConnectivityManager.TYPE_WIFI == connectivityManager.getActiveNetworkInfo().getType();
    }

    /**
     * 打开网络设置界面
     * @param activity activity
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}
