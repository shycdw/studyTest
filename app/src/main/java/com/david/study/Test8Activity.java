package com.david.study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by DavidChen on 2016/10/17.
 */
public class Test8Activity extends AppCompatActivity {
    static {
        System.loadLibrary("JNITest");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test8);
        Log.i("aaaaaaaaaaaaaaaa", "onCreate: " + 11111);
        String str = JNITest.printJNI();
        Log.i("aaaaaaaaaaaaaaaa", "onCreate: " + str);
    }
}
