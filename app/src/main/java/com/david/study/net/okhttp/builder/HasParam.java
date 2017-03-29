package com.david.study.net.okhttp.builder;

import java.util.Map;

/**
 * 增加参数的接口，因为有些请求不需要添加参数，如直接postString的方式，则不要添加，则不需要实现此接口
 * Created by DavidChen on 2016/11/9.
 */
public interface HasParam {
    /**
     * 添加参数
     */
    OkHttpRequestBuilder params(Map<String, String> params);

    /**
     * 添加单个参数
     */
    OkHttpRequestBuilder addParam(String key, String value);
}
