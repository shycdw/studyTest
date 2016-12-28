package com.david.study.net;/*
package com.david.study.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

*/
/**
 * 封装OkHttp工具类
 * Created by DavidChen on 2015/12/30.
 *//*

public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    public static final String ERROR = "error";

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enable
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        //连接超时
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    */
/**
     * 同步GET请求
     *
     * @param url
     * @return
     * @throws IOException
     *//*

    private Response get(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    */
/**
     * 同步GET请求
     *
     * @param url
     * @return
     * @throws IOException
     *//*

    private String getString(String url) throws IOException {
        Response response = get(url);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return ERROR;
        }
    }

    */
/**
     * 异步的GET请求
     *
     * @param url
     * @param callback
     *//*

    private void getAsyn(String url, final ResultCallback callback){
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    */
/**
     * 同步的POST请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     *//*

    private Response post(String url, Param... params) throws IOException
    {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    */
/**
     * 同步的POST请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     *//*

    private String postString(String url, Param... params) throws IOException
    {
        Response response = post(url, params);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return ERROR;
        }
    }

    */
/**
     * 异步的POST请求
     *
     * @param url
     * @param callback
     * @param params
     *//*

    private void postAsyn(String url, final ResultCallback callback, Param... params)
    {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    */
/**
     * 异步post请求
     *
     * @param url
     * @param callback
     * @param params
     *//*

    private void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    */
/**
     * 同步基于post的文件上传
     *
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @return
     * @throws IOException
     *//*

    private Response postFile(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postFile(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postFile(String url, File file,String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    */
/**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param params
     * @throws IOException
     *//*

    private void postFileAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    */
/**
     * 异步基于POST请求上传文件，单文件上传不带参数
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     *//*

    private void postFileAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    */
/**
     * 异步基于POST的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKeys
     * @param params
     * @throws IOException
     *//*

    private void postFileAsyn(String url, ResultCallback callback, File file, String fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKeys}, params);
        deliveryResult(callback, request);
    }

    */
/**
     * 异步下载文件
     * @param url
     * @param destFileDir 本地存储的文件夹
     * @param callback
     *//*

    private void downloadAsyn(final String url, final String destFileDir,final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buff = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                    }
                    fos.flush();
                    //如果下载成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    */
/**
     * 根据路径获取文件名称
     *
     * @param path
     * @return
     *//*

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return separatorIndex < 0 ? path :path.substring(separatorIndex + 1, path.length());
    }

    */
/**
     * 加载图片
     *
     * @param view
     * @param url
     * @param errorResId 加载错误显示的图片
     *//*

    private void displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                    ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                    int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = get(url);
                        //TODO adjust which it is.
                        is = response.body().byteStream();
                    }

                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    */
/**
     * 对外公布的方法
     *//*

    public static Response doGet(String url) throws IOException {
        return getInstance().get(url);
    }

    public static String doGetString(String url) throws IOException {
        return getInstance().getString(url);
    }

    public static void doGetAysn(String url, ResultCallback callback) {
        getInstance().getAsyn(url, callback);
    }

    public static Response doPost(String url, Param... params) throws IOException {
        return getInstance().post(url, params);
    }

    public static String doPostString(String url, Param... params) throws IOException {
        return getInstance().postString(url, params);
    }

    public static void doPostAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance().postAsyn(url, callback, params);
    }

    public static void doPostAsyn(String url, final ResultCallback callback, Map<String, String> params){
        getInstance().postAsyn(url, callback, params);
    }

    public static Response doPostFile(String url, File[] files, String[] fileKeys, Param... params) throws IOException{
        return getInstance().postFile(url, files, fileKeys, params);
    }

    public static Response doPostFile(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance().postFile(url, file, fileKey, params);
    }

    public static Response doPostFile(String url, File file, String fileKey) throws IOException {
        return getInstance().postFile(url, file, fileKey);
    }

    public static void doPostFileAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance().postFileAsyn(url, callback, files, fileKeys, params);
    }

    public static void doPostFileAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance().postFileAsyn(url, callback, file, fileKey);
    }

    public static void doPostFileAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance().postFileAsyn(url, callback, file, fileKey, params);
    }

    public static void doDownloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance().downloadAsyn(url, destDir, callback);
    }

    public static void doDisplayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance().displayImage(view, url, errorResId);
    }

    public static void doDisplayImage(final ImageView view, String url) throws IOException {
        getInstance().displayImage(view, url, -1);
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";;
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        return params == null ? new Param[0] : params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e) {//Json解析的错误
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onResponse(object);
            }
        });
    }

    public static abstract class ResultCallback<T>
    {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subClass) {
            Type superClass = subClass.getGenericSuperclass();
            if (superClass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter");
            }
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);
        public abstract void onResponse(T response);
    }

    public static class Param {
        String key;
        String value;
        public Param() {}
        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
*/
