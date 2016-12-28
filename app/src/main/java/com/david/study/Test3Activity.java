package com.david.study;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 * Created by DavidChen on 2016/8/3.
 */
public class Test3Activity extends AppCompatActivity {
    ListView lv_test;
    TextView tv_test1;
    LinearLayout l11;
    /*private WaveView waveView;*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "大卫"+i);
            map.put("company", "智农云禽");
            data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
                new String[]{"name", "company"}, new int[]{R.id.name, R.id.company});

        lv_test = (ListView) findViewById(R.id.lv_test);
        /*tv_test1 = (TextView) findViewById(R.id.tv_test1);
        l11 = (LinearLayout) findViewById(R.id.l11);*/
        lv_test.setAdapter(adapter);

        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 2f);
        lv_test.setLayoutAnimation(controller);

       /* waveView = (WaveView) findViewById(R.id.waveView);
        waveView.startWave();*/
    }

    public void test(View view) {
        if (lv_test.getVisibility() != View.VISIBLE) {
            tv_test1.setVisibility(View.GONE);
            lv_test.setVisibility(View.VISIBLE);
        } else {
            tv_test1.setVisibility(View.VISIBLE);
            lv_test.setVisibility(View.GONE);
        }
    }
}
