package com.david.study.ui.presenter;

import com.david.study.ui.view.IBaseView;

/**
 * presenter基类
 * Created by DavidChen on 2017/3/2.
 */

public class BasePresenter<T extends IBaseView> {

    protected T mView;

    protected BasePresenter(T view) {
        this.mView = view;
    }

    /**
     * 防止造成内存泄漏，其实用LeakCanary进行过检测，发现并没有出现过内存泄漏的情况，因此也可以不使用这个
     */
    public void onDestroy() {
        mView = null;
    }
}
