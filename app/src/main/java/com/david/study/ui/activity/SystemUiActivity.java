package com.david.study.ui.activity;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.david.study.R;

/**
 * 系统UI测试
 * Created by DavidChen on 2017/5/31.
 */

public class SystemUiActivity extends AppCompatActivity {
    private static final String TAG = "SystemUiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);*/
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        /*if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }*/

        /*//4.1以上可以设置布局在StatusBar后面的布局，但是要注意，不要让StatusBar覆盖到布局
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        // 这个与SYSTEM_UI_FLAG_FULLSCREEN区别在于，这个会使布局包含在statusBar下面，而且有statusBar。SYSTEM_UI_FLAG_FULLSCREEN是与StatusBar分开，且不含statusBar。
        decorView.setSystemUiVisibility(uiOptions);*/

        //4.4以上可以设置透明statusBar布局，但是要注意，不要让StatusBar覆盖到布局，与属性android:windowTranslucentStatus一致
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        /*View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        // 这个与SYSTEM_UI_FLAG_FULLSCREEN区别在于，这个会使布局包含在statusBar下面，而且有statusBar。SYSTEM_UI_FLAG_FULLSCREEN是与StatusBar分开，且不含statusBar。
        *//*decorView.setSystemUiVisibility(uiOptions);*//*
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 自动设置了SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN和SYSTEM_UI_FLAG_LAYOUT_STABLE标志
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.system_ui);
    }
        @Override
        public boolean onTouchEvent (MotionEvent event){
        /*View decorView = getWindow().getDecorView();
        // Calling setSystemUiVisibility() with a value of 0 clears all flags.
        decorView.setSystemUiVisibility(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });

            }
        }).start();*/
            return super.onTouchEvent(event);
        }

        @Override
        protected void onResume () {
            super.onResume();
            Log.i(TAG, "onResume: ");
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);*/
        }
    }
