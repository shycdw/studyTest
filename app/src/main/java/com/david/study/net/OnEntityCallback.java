package com.david.study.net;

/**
 * 网络请求回调
 * Created by DavidChen on 2016/9/13.
 */
public interface OnEntityCallback<T> {
    /**
     * 加载成功
     * @param entity 返回的实体
     */
    void onSuccess(T entity);

    /**
     * 加载失败
     * @param message 错误信息
     */
    void onFailure(String message);
}
