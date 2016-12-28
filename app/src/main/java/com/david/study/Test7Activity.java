package com.david.study;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.study.view3.PinnedHeaderExpandableListView;
import com.david.study.view3.StickyLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidChen on 2016/10/17.
 */
public class Test7Activity extends AppCompatActivity {
    FixedLayout fixedLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test7);
        ImageView iv_image, iv_image1;
        /*ListView listView;
        listView.setOnItemClickListener();*/
        /*fixedLayout = (FixedLayout) findViewById(R.id.fixedLayout);
        List<String> items = new ArrayList<>();
        for (int i= 0; i < 5; i++) {
            items.add("条目" + i);
        }
        fixedLayout.setItem(items);*/
         /*iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Test7Activity.this, Test6Activity.class));
            }
        });
        iv_image1 = (ImageView) findViewById(R.id.iv_image1);
        iv_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Test7Activity.this, Test8Activity.class));
            }
        });*/
        final String[] group = new String[] {"A", "B", "C", "D", "E", "F"};
        final List<List<String>> child = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<String> l = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                l.add(group[i] + j);
            }
            child.add(l);
        }
        final PinnedHeaderExpandableListView elv_test = (PinnedHeaderExpandableListView) findViewById(R.id.elv_test);
        elv_test.setAdapter(new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return group.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return child.get(groupPosition).size();
            }

            @Override
            public String getGroup(int groupPosition) {
                return group[groupPosition];
            }

            @Override
            public String getChild(int groupPosition, int childPosition) {
                return child.get(groupPosition).get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                TextView textView = new TextView(Test7Activity.this);
                textView.setBackgroundColor(Color.GRAY);
                textView.setPadding(32, 32, 32, 32);
                textView.setText(getGroup(groupPosition));
                return textView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(Test7Activity.this);
                textView.setPadding(32, 32, 32, 32);
                textView.setText(getChild(groupPosition, childPosition));
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }
        });
        elv_test.setOnHeaderUpdateListener(new PinnedHeaderExpandableListView.OnHeaderUpdateListener() {
            TextView textView;
            @Override
            public View getPinnedHeader() {
                textView = new TextView(Test7Activity.this);
                textView.setBackgroundColor(Color.GRAY);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setPadding(32, 32, 32, 32);
                return textView;
            }

            @Override
            public void updatePinnedHeader(int firstVisibleGroup) {
                textView.setText(elv_test.getExpandableListAdapter().getGroup(firstVisibleGroup).toString());
            }
        });
        StickyLayout stick = (StickyLayout) findViewById(R.id.stick);
        stick.setOnGiveUpTouchListener(new StickyLayout.OnGiveUpTouchListener() {
            @Override
            public boolean giveUpTouchEvent() {
                if (elv_test.getFirstVisiblePosition() == 0) {
                    View view = elv_test.getChildAt(0);
                    if (view != null && view.getTop()  >= 0) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void getItems(View view) {
        Log.i("TAG", fixedLayout.getSelectedContent());
    }

}
