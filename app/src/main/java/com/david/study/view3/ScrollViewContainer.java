package com.david.study.view3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by DavidChen on 2016/10/27.
 */
public class ScrollViewContainer extends LinearLayout {

    private Scroller mScroller;
    private View mTopView;
    private int mTopHeight;
    private View mBottomView;

    public ScrollViewContainer(Context context) {
        super(context);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
