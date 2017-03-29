package com.david.study.net.okhttp.request;

import com.david.study.net.okhttp.builder.PostFormBuilder;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * post方式提交表单
 * Created by DavidChen on 2016/11/9.
 */
public class PostFormRequest extends OkHttpRequest {

    private List<PostFormBuilder.FileInput> mFiles;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files) {
        super(url, tag, params, headers);
        this.mFiles = files;
    }

    @Override
    RequestBody buildRequestBody() {
        if (mFiles == null || mFiles.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            addParams(builder);
            for (int i = 0; i < mFiles.size(); i++)
            {
                PostFormBuilder.FileInput fileInput = mFiles.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.mFilename)), fileInput.mFile);
                builder.addFormDataPart(fileInput.mKey, fileInput.mFilename, fileBody);
            }
        }

        return null;
    }

    @Override
    Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    private void addParams(MultipartBody.Builder builder) {
        if (mParams != null && !mParams.isEmpty()) {
            for (String key : mParams.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, mParams.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (mParams != null) {
            for (String key : mParams.keySet()) {
                builder.add(key, mParams.get(key));
            }
        }
    }

    private String guessMimeType(String filepath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(filepath, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
