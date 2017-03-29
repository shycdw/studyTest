package com.david.study.net.okhttp;

import com.david.study.net.okhttp.builder.GetBuilder;
import com.david.study.net.okhttp.builder.PostFormBuilder;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * OkHttp工具类（参考鸿洋大神的OkHttpUtils）
 * 采用单例模式（双重检查加锁方式）
 * OkHttp请求的过程：RequestBody->Request->Call->Call.enqueue（callback）,
 * OkHttp请求的过程中，RequestBody的构建（不同的参数类型等）以及Request的
 * 构建（不同的请求方式）是变化因素，因此分别进行抽象。
 * 采用Builder模式对RequestBody的参数进行统一构造。采用模板方法模式对RequestBody以及
 * Request的构建进行抽象，最后统一生成call。同时对回调结果的转换进行了封装，
 * 实现callback接口即可使用对应的回调。
 * 另外，由于enqueue()方法是在新的线程进行执行，则回调结果需要使用handler更新到MainThread。
 * 但是这个handler的过程已经封装好，用户不必考虑这一点。
 * example：
 * OkHttpUtils.getInstance()
 *            .get()
 *            .url(url)
 *            .tag(this)
 *            .addParam("key", "value")
 *            .build()
 *            .execute(new StringCallback() {
 *                public void onResponse(String response) {}
 *                public void onError(Call call, Exception e) {}
 *            });
 *
 * @author DavidChen
 *          Created by DavidChen on 2016/11/9.
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10_000L;

    private OkHttpClient mOkHttpClient;
    private volatile static OkHttpUtils sInstance;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpUtils getInstance() {
        return initClient();
    }

    private static OkHttpUtils initClient() {
        if (sInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpUtils();
                }
            }
        }
        return sInstance;
    }

    public PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public GetBuilder get() {
        return new GetBuilder();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }
}
