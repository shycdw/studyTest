package com.david.study.view3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义仿淘宝下拉加载详情的控件，内部可以包含俩子View（ViewGroup）
 * Created by DavidChen on 2016/11/18.
 */
public class DoubleScrollView extends ViewGroup {

    private static final int STATUS_UP = 0;
    private static final int STATUS_DOWN = 1;
    private static final int SNAP_VELOCITY = 1000;

    private int mPerHeight;
    private int mWidth;
    private int mFirstY;
    private int mFirstX;
    private int mLastY;
    private int mStatus = STATUS_UP;
    private int mTouchSlop;
    private Scroller mScroller;
    private VelocityTracker mTracker;
    private OnGiveUpTouchListener mListener;

    public DoubleScrollView(Context context) {
        this(context, null);
    }

    public DoubleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = x;
                mFirstY = mLastY = y;
                if (mScroller != null && !mScroller.isFinished()) { // 按下时动画没完成也拦截
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mFirstX;
                int deltaY = y - mFirstY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {  // 如果横向大于纵向，则不拦截
                    intercept = false;
                } else if (mStatus == STATUS_UP && deltaY < -mTouchSlop && mListener != null) {
                    if (mListener.onAboveToBottom()) {  // 如果处于顶部，且向上滑动距离超过最小拦截距离，且顶部已经到底，则拦截
                        intercept = true;
                    }
                } else if (mStatus == STATUS_DOWN && deltaY > mTouchSlop && mListener != null) {
                    if (mListener.onBelowToTop()) { // 如果处于底部，且向下滑动距离超过最小拦截距离，且底部已经到顶，则拦截
                        intercept = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;  // up不用拦截，因为如果前面move拦截了，则一定会在up处传递到onTouchEvent中，且，此处如果拦截了，则其他的事件就没有up事件可用
                mFirstX = mFirstY = mLastY = 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastY - y;
                // 如果y偏移距离大于0且小于顶部容器高度，则滑动
                if (deltaY + getScrollY() > 0 && deltaY + getScrollY() < mPerHeight) {
                    scrollBy(0, deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                mTracker.computeCurrentVelocity(1000);
                int velocityY = (int) mTracker.getYVelocity();
                // 首先根据移速判断，如果速度在[-1000, 1000]之内则根据滑动距离来判断
                if (velocityY < -SNAP_VELOCITY) {  // velocity（X或Y）负为手指向左向上滑动
                    smoothScrollTo(0, mPerHeight - getScrollY());
                    mStatus = STATUS_DOWN;
                } else if (velocityY > SNAP_VELOCITY) { // velocity（X或Y）正为手指向右向下滑动
                    smoothScrollTo(0, -getScrollY());
                    mStatus = STATUS_UP;
                } else {    // 根据已经滑动的距离来判断
                    if (getScrollY() > mPerHeight / 2) {    // 当滚动距离超过一半时，则滚动到底部
                        smoothScrollTo(0, mPerHeight - getScrollY());
                        mStatus = STATUS_DOWN;
                    } else {    // 当滚动距离小于一般时，则滚动到顶部
                        smoothScrollTo(0, -getScrollY());
                        mStatus = STATUS_UP;
                    }
                }
                break;
        }
        mLastY = y;
        return true;
    }

    /**
     * 平滑滚动到指定位置
     *
     * @param dx 要滑动的x轴距离，正为左
     * @param dy 要滑动的y轴距离，正为上
     */
    private void smoothScrollTo(int dx, int dy) {
        mScroller.startScroll(0, getScrollY(), dx, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        // 判断是否已经滚动到指定位置
        if (mScroller.computeScrollOffset()) {
            // 滚动到新位置
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 重绘
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() != 2) {
            throw new IllegalStateException("Only two child views are allowed. ");
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量高度
     * 此处需要理解作为容器，此处做限制，只允许设置match_parent或者确定值，而且高度要为准确高度的2倍
     */
    private int measureHeight(int heightMeasureSpec) {
        int height;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("Layout_height must be exactly.");
        } else {
            mPerHeight = size;
            height = 2 * mPerHeight;
        }
        return height;
    }

    /**
     * 测量宽度
     * 此处需要理解因为作为容器，所以此处做限制，只允许设置match_parent或者确定值
     */
    private int measureWidth(int widthMeasureSpec) {
        int width;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("Layout_width must be exactly.");
        } else {
            mWidth = size;
            width = mWidth;
        }
        return width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View top = getChildAt(0);
        if (top != null) {
            top.layout(0, 0, mWidth, mPerHeight);
        }
        View bottom = getChildAt(1);
        if (bottom != null) {
            bottom.layout(0, mPerHeight, mWidth, 2 * mPerHeight);
        }
    }

    public void setOnGiveUpTouchListener(OnGiveUpTouchListener onGiveUpTouchListener) {
        this.mListener = onGiveUpTouchListener;
    }

    /**
     * 用于判断上下两个布局是否到底或者到顶的接口
     */
    public interface OnGiveUpTouchListener {

        /**
         * 当顶部view滑动到底部
         *
         * @return true, 可以上拉底部内容，false，不可以
         */
        boolean onAboveToBottom();

        /**
         * 当底部view滑动到顶部
         *
         * @return true, 可以下拉到顶部内容，false，不可以
         */
        boolean onBelowToTop();
    }
}
