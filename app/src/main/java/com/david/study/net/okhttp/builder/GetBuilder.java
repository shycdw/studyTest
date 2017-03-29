package com.david.study.net.okhttp.builder;

import android.net.Uri;

import com.david.study.net.okhttp.request.GetRequest;
import com.david.study.net.okhttp.request.OkHttpRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * get请求参数封装
 *
 * @author DavidChen
 *         Created by DavidChen on 2016/11/9.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParam {

    @Override
    public OkHttpRequest build() {
        if (mParams != null) {
            mUrl = appendParams(mUrl, mParams);
        }
        return new GetRequest(mUrl, mTag, mParams, mHeaders);
    }

    /**
     * 拼接参数到url上
     *
     * @param url    url
     * @param params 参数
     * @return 拼接好的url
     */
    private String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    @Override
    public GetBuilder params(Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    @Override
    public GetBuilder addParam(String key, String value) {
        if (this.mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, value);
        return this;
    }
}
