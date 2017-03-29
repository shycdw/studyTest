package com.david.study.net.okhttp.request;

import android.os.Handler;
import android.os.Looper;

import com.david.study.net.okhttp.OkHttpUtils;
import com.david.study.net.okhttp.callback.Callback;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 请求基类，只用于定义一些基本的请求方式（post，get等），其他具体的业务上的请求请另行封装
 *
 * OkHttp请求构建过程：
 * 1.初始化基本参数，包括url，tag，参数，header
 *
 * @author DavidChen
 *         Created by DavidChen on 2016/11/9.
 */
public abstract class OkHttpRequest {
    private Call mCall;

    private long mReadTimeOut;
    private long mWriteTimeOut;
    private long mConnTimeOut;

    private String mUrl;
    private Object mTag;
    protected Map<String, String> mParams;
    private Map<String, String> mHeaders;

    protected Request.Builder builder = new Request.Builder();

    private final Handler handler = new Handler(Looper.getMainLooper());

    public OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        this.mUrl = url;
        this.mTag = tag;
        this.mParams = params;
        this.mHeaders = headers;
        if (mUrl == null) {
            throw new IllegalArgumentException("url can not be null.");
        }
        initBuilder();
    }

    private void initBuilder() {
        builder.url(mUrl).tag(mTag);
        appendHeaders();
    }

    /**
     * 拼装header
     */
    private void appendHeaders() {
        if (mHeaders != null && mHeaders.size() > 0) {
            Headers.Builder headerBuilder = new Headers.Builder();
            for (String key : mHeaders.keySet()) {
                headerBuilder.add(key, mHeaders.get(key));
            }
            builder.headers(headerBuilder.build());
        }
    }

    public long getConnTimeOut() {
        return mConnTimeOut;
    }

    public void setConnTimeOut(long connTimeOut) {
        this.mConnTimeOut = connTimeOut;
    }

    public long getWriteTimeOut() {
        return mWriteTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.mWriteTimeOut = writeTimeOut;
    }

    public long getReadTimeOut() {
        return mReadTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.mReadTimeOut = readTimeOut;
    }

    /**
     * 构建请求体
     */
    abstract RequestBody buildRequestBody();

    /**
     * 构建请求
     */
    abstract Request buildRequest(RequestBody requestBody);

    public Call buildCall() {
        Request mRequest = generateRequest();
        if (mReadTimeOut > 0 || mWriteTimeOut > 0 || mConnTimeOut > 0) {
            mReadTimeOut = mReadTimeOut > 0 ? mReadTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            mWriteTimeOut = mWriteTimeOut > 0 ? mWriteTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            mConnTimeOut = mConnTimeOut > 0 ? mConnTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            mCall = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(mConnTimeOut, TimeUnit.MILLISECONDS)
                    .build().newCall(mRequest);
        } else {
            mCall = OkHttpUtils.getInstance().getOkHttpClient().newCall(mRequest);
        }
        return mCall;
    }

    private Request generateRequest() {
        RequestBody requestBody = buildRequestBody();
        return buildRequest(requestBody);
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    public void execute(Callback callback) {
        buildCall();
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        mCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        finalCallback.onError(call, e);
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        return;
                    }
                    if (!finalCallback.validateResponse(response)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                finalCallback.onError(call, new IOException("request failed,response code is :" + response.code()));
                            }
                        });
                    }
                    final Object o = finalCallback.pareResponse(response);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalCallback.onResponse(o);
                        }
                    });

                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalCallback.onError(call, e);
                        }
                    });
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    public Response execute() throws IOException {
        buildCall();
        return mCall.execute();
    }
}
