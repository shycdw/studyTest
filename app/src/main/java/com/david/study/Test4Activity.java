package com.david.study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.david.study.company.WaveView;

import okhttp3.OkHttpClient;

/**
 * 测试
 * Created by DavidChen on 2016/8/24.
 */
public class Test4Activity extends AppCompatActivity {
    private WaveView vw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        OkHttpClient client;
    }
}
