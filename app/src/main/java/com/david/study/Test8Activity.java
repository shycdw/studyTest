package com.david.study;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.david.study.ui.fragment.TestFragment;

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
        TestFragment testFragment = new TestFragment();
        EditText e1 = (EditText) findViewById(R.id.e1);
        EditText e2 = (EditText) findViewById(R.id.e2);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("123123", "afterTextChanged: " + s.toString());
                if (!TextUtils.isEmpty(s.toString()) && TextUtils.isDigitsOnly(s.toString())) {
                    Log.i("123123", "afterTextChanged-int: " + Integer.parseInt(s.toString()));
                }
                if (!TextUtils.isEmpty(s.toString()) && TextUtils.isDigitsOnly(s.toString())) {
                    Log.i("123123", "afterTextChanged-float: " + Float.parseFloat(s.toString()));
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Dialog dialog = new Dialog(Test8Activity.this, R.style.dialog);
        dialog.setContentView(R.layout.test);
        dialog.show();
    }
}
