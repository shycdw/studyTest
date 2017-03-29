package com.david.study.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 管理类
 *
 * @author DavidChen
 *         Created by DavidChen on 2017/2/27.
 */

public class ActivityCollector {

    /**
     * activity集合
     */
    private static List<Activity> activities = new ArrayList<>();

    /**
     * 新增Activity
     * @param activity 活动
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除Activity
     * @param activity 活动
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 退出App
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
