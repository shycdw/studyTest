package com.david.study.net;

import com.david.study.biz.BaseBiz;

import java.util.List;

/**
 * 使用的http框架封装，策略模式，使用该接口将请求框架与请求解耦
 * 由于本应用内部框架很混乱（网络请求就有四个框架。。。），所以暂时放在这里
 * Created by DavidChen on 2016/9/19.
 */
public interface IHttpClient {
    /**
     * 发送http请求
     * @param url url
     * @param params 参数list,由子类自己进行拼接
     * @param tag 用于取消请求
     * @param onStringCallback 回调
     */
    void doPost(String url, List<BaseBiz.Param> params, Object tag, final BaseBiz.OnStringCallback onStringCallback);

    void doGet(String url, Object tag, final BaseBiz.OnStringCallback onStringCallback);
}
