package com.david.study.net.okhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get方式请求类
 * Created by DavidChen on 2016/11/9.
 */
public class GetRequest extends OkHttpRequest {

    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    RequestBody buildRequestBody() {
        return null;
    }

    @Override
    Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }
}
