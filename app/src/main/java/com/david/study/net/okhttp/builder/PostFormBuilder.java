package com.david.study.net.okhttp.builder;

import com.david.study.net.okhttp.request.OkHttpRequest;
import com.david.study.net.okhttp.request.PostFormRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * post方式提交表单的参数构造
 * Created by DavidChen on 2016/11/9.
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParam {

    private List<FileInput> mFiles = new ArrayList<>();

    @Override
    public PostFormBuilder params(Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    @Override
    public PostFormBuilder addParam(String key, String value) {
        if (this.mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, value);
        return this;
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.mFiles.add(new FileInput(key, filename, files.get(key)));
        }
        return this;
    }

    public PostFormBuilder addFile(String key, String filename, File file) {
        mFiles.add(new FileInput(key, filename, file));
        return this;
    }

    @Override
    public OkHttpRequest build() {
        return new PostFormRequest(mUrl, mParams, mParams, mHeaders, mFiles);
    }

    public static class FileInput {
        public String mKey;
        public String mFilename;
        public File mFile;

        public FileInput(String key, String filename, File file) {
            this.mKey = key;
            this.mFilename = filename;
            this.mFile = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + mKey + '\'' +
                    ", filename='" + mFilename + '\'' +
                    ", file=" + mFile +
                    '}';
        }
    }
}
