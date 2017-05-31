package com.david.study.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.david.study.R;
import com.example.myweather.ui.SystemBarTintManager;

/**
 * 启动界面
 * Created by DavidChen on 2015/12/8.
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView iv_start_view_bg;
    private ImageView iv_start_view_icon;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_ui);

        initTintStatusBar();

    }

    /**
     * 设置系统沉浸式状态栏
     */
    private void initTintStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        // 设置颜色
        // tintManager.setStatusBarTintColor(Color.parseColor("#0000CC"));
        // 设置图片
        // tintManager.setStatusBarTintDrawable(drawable);

        //设置资源文件，0， 设置状态栏无背景
        tintManager.setStatusBarTintResource(0);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            params.flags |= bits;
        } else {
            params.flags &= ~bits;
        }
        win.setAttributes(params);
    }

    //禁用back键
    @Override
    public void onBackPressed() {
    }
}
