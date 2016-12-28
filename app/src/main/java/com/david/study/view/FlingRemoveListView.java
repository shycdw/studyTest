package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * 支持item侧滑的ListView
 * Created by DavidChen on 2016/7/21.
 */
public class FlingRemoveListView extends ListView {
    private VelocityTracker mVelocityTracker;
    private float mPreX;//上一个滑动点的坐标
    private float mFirstX,mFirstY;//第一次按下的点的x坐标和y坐标
    private View mWillFlingView;//要滑动的列表项View
    private int mPosition = INVALID_POSITION;//要滑动的列表项View的索引位置
    private int mTouchSlop;//滑动最小距离
    private static final int SNAP_VELOCITY = 600;
    private boolean mIsSlide;

    public FlingRemoveListView(Context context) {
        this(context, null);
    }

    public FlingRemoveListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingRemoveListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = mPreX = x;
                mFirstY = y;
                //确定列表项
                mPosition = this.pointToPosition(x, y);
                if (mPosition != INVALID_POSITION) {
                    int visibleIndex = mPosition - getFirstVisiblePosition();
                    mWillFlingView = getChildAt(visibleIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float xVelocity = mVelocityTracker.getXVelocity();
                //当速度大于最低速度或者x方向移动距离大于最小移动距离，
                // 并且y方向小于最小移动距离时则可以进行左右滑动
                if (Math.abs(xVelocity) > SNAP_VELOCITY || Math.abs(x - mFirstX) > mTouchSlop
                        && Math.abs(y - mFirstY) < mTouchSlop)
                    mIsSlide = true;
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {//释放并回收资源
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsSlide && mPosition != INVALID_POSITION) {
            float x = ev.getX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float dx = mPreX - x;
                    mWillFlingView.scrollBy((int) dx, 0);
                    mPreX = x;
                    break;
                case MotionEvent.ACTION_UP:
                    mWillFlingView = null;
                    mPosition = INVALID_POSITION;
                    mIsSlide = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }
}
