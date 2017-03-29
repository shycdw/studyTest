package com.david.study.ui.view;

/**
 * MVP,View 基类
 * Created by DavidChen on 2017/3/2.
 */

public interface IBaseView {
    /**
     * 显示正在加载进度条
     */
    void showLoading();

    /**
     * 清除加载进度条
     */
    void hideLoading();

    /**
     * 显示Toast
     *
     * @param message 要toast显示的信息或者使用SnackBar
     */
    void showMessage(String message);
}
