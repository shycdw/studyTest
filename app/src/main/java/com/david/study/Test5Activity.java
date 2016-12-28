package com.david.study;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.david.study.company.SimpleTabStripView;
import com.david.study.company.TestFragment;

import java.util.ArrayList;

/**
 * test5
 * Created by DavidChen on 2016/9/2.
 */
public class Test5Activity extends AppCompatActivity {
    private SimpleTabStripView tab_strip;
    private ViewPager viewPager;
    ArrayList<TestFragment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final CharSequence[] titles = getResources().getTextArray(R.array.test);
        tab_strip = (SimpleTabStripView) findViewById(R.id.tab_strip);
        for (int i = 0; i < titles.length; i++) {
            TestFragment fragment = new TestFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("titles", titles[i]);
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });
        tab_strip.setViewPager(viewPager);
        /*tab_strip.setTitles(titles);*/
        Handler mHandler = new Handler();
        /*mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tab_strip.setSelectedTxtColor(Color.BLUE);
                tab_strip.setUnselectedTxtColor(Color.RED);
            }
        }, 4000);*/
    }
}
