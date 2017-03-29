package com.david.study.net.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 将请求结果转换成String类型的回调
 * Created by DavidChen on 2016/11/9.
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String pareResponse(Response response) throws IOException {
        return response.body().string();
    }
}
