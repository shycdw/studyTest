package com.david.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.david.study.view.FilingRemovedListView;
import com.david.study.view.LeftSlideView;
import com.david.study.view.SliderMenu;
import com.david.study.view.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用activity
 * Created by DavidChen on 2016/7/19.
 */
public class TestActivity extends AppCompatActivity {
    /*LeftSlideView leftSlideView;
    SliderMenu sliderMenu;
    SlidingMenu slidingMenu;
    FilingRemovedListView lv;
    List<Map<String, String>> list = new ArrayList<>();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*leftSlideView = (LeftSlideView) findViewById(R.id.leftSlideView);*//*
        *//*sliderMenu = (SliderMenu) findViewById(R.id.sliderMenu);*//*
        lv = (FilingRemovedListView) findViewById(R.id.lv);
        lv.setOnRemovedItemListener(new FilingRemovedListView.OnRemovedItemListener() {
            @Override
            public void itemRemoved(int position, ListAdapter adapter) {
                list.remove(position);
                ((BaseAdapter) adapter).notifyDataSetChanged();
            }
        });*/
        init();
    }

    private void init() {
        /*for (int i = 0; i < 50; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "大卫"+i);
            map.put("company", "智农云禽");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item,
                new String[]{"name", "company"}, new int[]{R.id.name, R.id.company});
        lv.setAdapter(adapter);*/
    }

    public void toggle(View view) {
        /*sliderMenu.toggle();*//*
        *//*leftSlideView.toggle();*//*
        slidingMenu.toggle();*/
    }
    public void switchClick(View view) {
        Log.i("TestActivity2", "switchClick");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    Intent intent = new Intent(TestActivity.this, TestActivity2.class);
                    intent.putExtra("aa", (i++)+"");
                    Log.i("TestActivity2", ""+i);
                    startActivity(intent);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
       /* leftSlideView.toggle();*/
    }
}
