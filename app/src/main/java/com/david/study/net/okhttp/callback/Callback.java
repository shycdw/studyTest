package com.david.study.net.okhttp.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 回调基类
 * Created by DavidChen on 2016/11/8.
 */
public abstract class Callback<T> {

    /**
     * 判断response返回值是否合法,[200-300)
     */
    public boolean validateResponse(Response response) {
        return response != null && response.isSuccessful();
    }

    /**
     * 根据类型转换response
     */
    public abstract T pareResponse(Response response) throws IOException;

    /**
     * 请求返回结果时回调
     */
    public abstract void onResponse(T response);

    /**
     * 请求连接失败时回调
     */
    public abstract void onError(Call call, Exception e);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object pareResponse(Response response) {
            return null;
        }

        @Override
        public void onResponse(Object response) {

        }

        @Override
        public void onError(Call call, Exception e) {

        }
    };
}
