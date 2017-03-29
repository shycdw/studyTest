package com.david.study.biz;

import android.util.Log;

import com.david.study.net.IHttpClient;
import com.david.study.net.OkHttpClient;
import com.david.study.net.OnEntityCallback;
import com.david.study.util.JsonUtils;
import com.david.study.util.L;

import java.util.List;

/**
 * 业务逻辑基类，使用模板方法模式(学习)，重写handleResult(json)来取出实体bean
 * 此基类目的是封装业务逻辑的网络请求，从post请求到的json字符串中取出实体bean，
 * 更重要的是将网络请求框架与请求解耦（这是后面想到的，前期只是想取出bean）
 * Created by DavidChen on 2016/9/18.
 */
public abstract class BaseBiz<T> {

    protected static final String TAG = "BaseBiz";

    // 默认使用OkHttp框架
    private IHttpClient mHttpClient = new OkHttpClient();

    /**
     * 处理结果，返回实体bean,请自行判断是否有需要的值JsonUtils.hasMember(resultJson, "***")
     *
     * @return bean
     */
    abstract T handleResult(String resultJson);

    /**
     * 预处理结果字符串,默认不处理，应对有些特殊情况
     *
     * @param result 结果字符串
     * @return 处理之后的结果字符串(json格式)
     */
    protected String preProcessResult(String result) {
        return result;
    }

    /**
     * 发送http请求，模板方法模式，结果处理重写handleResult(String)
     *
     * @param url              请求地址
     * @param tag              取消请求tag
     * @param onEntityCallback 回调
     */
    protected void post(String url, List<Param> params, Object tag, final OnEntityCallback<T> onEntityCallback) {
        if (mHttpClient != null) {
            mHttpClient.doPost(url, params, tag, new OnStringCallback() {
                @Override
                public void onSuccess(String result) {
                    L.i(TAG, result);
                    try {
                        result = preProcessResult(result);
                        if ("0".equals(JsonUtils.getMember(result, "retcode"))) {
                            T bean = handleResult(result);
                            if (onEntityCallback != null) {
                                onEntityCallback.onSuccess(bean);
                            }
                        } else {
                            if (JsonUtils.hasMember(result, "message")) {
                                if (onEntityCallback != null) {
                                    onEntityCallback.onFailure(JsonUtils.getMember(result, "message"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (onEntityCallback != null) {
                            onEntityCallback.onFailure("数据解析异常");
                        }
                    }
                }

                @Override
                public void onFailure(String message) {
                    if (onEntityCallback != null) {
                        onEntityCallback.onFailure("网络或服务器异常");
                    }
                }
            });
        } else {
            throw new IllegalStateException("网络请求框架不能为空！");
        }
    }

    /**
     * 发送http请求，模板方法模式，结果处理重写handleResult(String)
     *
     * @param url              请求地址
     * @param onEntityCallback 回调
     */
    protected void get(String url, Object tag, final OnEntityCallback<T> onEntityCallback) {
        if (mHttpClient != null) {
            mHttpClient.doGet(url, tag, new OnStringCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, result);
                    result = preProcessResult(result);
                    T bean = handleResult(result);
                    if (onEntityCallback != null) {
                        onEntityCallback.onSuccess(bean);
                    }
                }

                @Override
                public void onFailure(String message) {
                    if (onEntityCallback != null) {
                        onEntityCallback.onFailure("网络连接异常");
                    }
                }
            });
        } else {
            throw new IllegalStateException("网络请求框架不能为空！");
        }
    }

    public static class Param {
        public String key;
        public String value;

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 网络请求回调
     */
    public interface OnStringCallback {
        /**
         * 网络请求成功
         */
        void onSuccess(String result);

        /**
         * 网络请求失败
         */
        void onFailure(String message);
    }
}
