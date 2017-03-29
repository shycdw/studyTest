package com.david.study.net;

import com.david.study.biz.BaseBiz;
import com.david.study.net.okhttp.OkHttpUtils;
import com.david.study.net.okhttp.builder.PostFormBuilder;
import com.david.study.net.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * XUtils工具
 * Created by DavidChen on 2016/9/19.
 */
public class OkHttpClient implements IHttpClient {
    @Override
    public void doPost(String url, List<BaseBiz.Param> params, Object tag, final BaseBiz.OnStringCallback onStringCallback) {
        PostFormBuilder builder = OkHttpUtils.getInstance()
                .post()
                .url(url)
                .tag(tag);
        for (BaseBiz.Param param : params) {
            builder.addParam(param.key, param.value);
        }
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        onStringCallback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onStringCallback.onFailure("网络连接异常");
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void doGet(String url, Object tag, final BaseBiz.OnStringCallback onStringCallback) {
        OkHttpUtils.getInstance().get().url(url).tag(tag).build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                onStringCallback.onSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                onStringCallback.onFailure("网络连接异常");
                e.printStackTrace();
            }
        });
    }
}
