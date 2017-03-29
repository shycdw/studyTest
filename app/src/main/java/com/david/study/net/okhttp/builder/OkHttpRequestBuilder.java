package com.david.study.net.okhttp.builder;

import com.david.study.net.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * Builder模式，构造参数
 *
 * @author DavidChen
 *         Created by DavidChen on 2016/11/9.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {

    protected String mUrl;
    protected Object mTag;
    protected Map<String, String> mParams;
    protected Map<String, String> mHeaders;

    public T url(String url) {
        this.mUrl = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.mTag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.mHeaders = headers;
        return (T) this;
    }

    /**
     * 构建OkHttpRequest
     */
    public abstract OkHttpRequest build();
}
