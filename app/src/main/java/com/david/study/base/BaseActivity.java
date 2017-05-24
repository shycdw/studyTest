package com.david.study.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.david.study.app.ActivityCollector;
import com.david.study.dialog.ProgressLoadingDialog;
import com.david.study.ui.view.IBaseView;

/**
 * activity基类
 *
 * @author DavidChen
 * @version 1.0 2017/2/27 说明：沉浸式状态栏，activity管理，抽象初始化步骤，实现mvp中view基类
 *          Created by DavidChen on 2017/2/27.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private ProgressLoadingDialog mProgressLoadingDialog; // 进度dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置内容在statusBar下面，要配合fitSystemWindows使用。
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initData();
        initEvent();
    }

    /**
     * 加载View
     */
    protected abstract void initView();

    /**
     * 添加事件监听器
     */
    protected abstract void initEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 显示正在加载进度条
     */
    @Override
    public void showLoading() {
        if (null == mProgressLoadingDialog) {
            mProgressLoadingDialog = new ProgressLoadingDialog.Builder(this).build();
        }
        mProgressLoadingDialog.show();
    }

    /**
     * 清除加载进度条
     */
    @Override
    public void hideLoading() {
        if (null != mProgressLoadingDialog) {
            mProgressLoadingDialog.dismiss();
        }
    }

    /**
     * 显示Toast
     *
     * @param message 要toast显示的信息或者使用SnackBar
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
