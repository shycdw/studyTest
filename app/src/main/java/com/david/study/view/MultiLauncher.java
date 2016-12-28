package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 触摸滑屏，测试速度跟踪器VelocityTracker
 * Created by DavidChen on 2016/7/20.
 */
public class MultiLauncher extends ViewGroup {
    private static final int TOUCH_STATE_STOP = 0x001;//停止状态
    private static final int TOUCH_STATE_FLING = 0x002; //滑动状态
    private static final int SNAP_VELOCITY = 1000;

    private int mTouchState = TOUCH_STATE_STOP;
    private Scroller mScroller;
    private int mTouchSlop = 0;//最小滑动距离，超过了才认为开始滑动
    private float lastMotionX = 0;//上次触摸x的位置
    private int mCurScreen;//当前屏幕index
    private VelocityTracker mTracker;//速率跟踪器

    public MultiLauncher(Context context) {
        this(context,null);
    }

    public MultiLauncher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLauncher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int n = getChildCount();
        int w = (r - l) / n;//分屏的宽度
        int h = b - t;//容器高度
        for (int i = 0; i < n; i++) {
            View child = getChildAt(i);
            child.layout(i * w, 0, (i + 1) * w, h);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        final int x = (int) ev.getX();
        if (action == MotionEvent.ACTION_MOVE && mTouchState == TOUCH_STATE_STOP)
            return true;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                lastMotionX = x;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_STOP : TOUCH_STATE_FLING;
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动距离过小不算滑动
                final int dx = (int) Math.abs(x - lastMotionX);//去距离差绝对值
                if (dx > mTouchSlop) {
                    mTouchState = TOUCH_STATE_FLING;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_STOP;
                break;
        }
        return mTouchState != TOUCH_STATE_STOP;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        super.onTouchEvent(event);
        final int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下时如果正在滑动，则停止滑动
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                lastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (lastMotionX - x);
                scrollBy(dx, 0);
                lastMotionX = x;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker tracker = this.mTracker;
                tracker.computeCurrentVelocity(1000);
                int velocityX = (int) tracker.getXVelocity();
                //通过判断velocityX的正负可以判断滑动方向
                if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                    moveToPrevious();
                } else if (velocityX < -SNAP_VELOCITY && mCurScreen < (getChildCount() - 1)) {
                    moveToNext();
                } else {
                    moveToDestination();
                }
                if (mTracker != null) {//释放资源
                    this.mTracker.clear();
                    this.mTracker.recycle();
                    this.mTracker = null;
                }
                mTouchState = TOUCH_STATE_STOP;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_STOP;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //设置容器内组件的新位置
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //重绘以刷新产生动画
            postInvalidate();
        }
    }

    /**
     * 惯性移动到指定的屏，（只负责滚动，不负责判断是否要滚动）
     * @param whichScreen 屏幕index
     */
    public void moveToScreen(int whichScreen) {
        mCurScreen = whichScreen;
        if (mCurScreen > getChildCount() - 1)
            mCurScreen = getChildCount() - 1;
        if (mCurScreen < 0)
            mCurScreen = 0;
        int scrollX = getScrollX();
        //每一屏的宽度
        int splitWidth = getWidth() / getChildCount();
        //要移动的距离,理解此句含义，（例子：当前操作是从第0屏移动到第1屏，则，当前屏mCurScreen = 1，
        // 也就是切换到1屏，总共要移动mCurScreen的距离，而此时已经移动了scrollX，则还要惯性滚动
        // mCurScreen * splitWidth - scrollX的距离）
        int dx = mCurScreen * splitWidth - scrollX;
        //开始移动
        mScroller.startScroll(scrollX, 0, dx, 0, Math.abs(dx));
        invalidate();
    }

    /**
     * 根据滚动距离判断是回滚还会滑动到下一页
     */
    public void moveToDestination() {
        //每一屏宽度
        int splitWidth = getWidth() / getChildCount();
        //判断回滚还是进入下一屏，意思为，如果已经滚动超过一半屏宽，则进入下一屏
        int toScreen = (getScrollX() + splitWidth / 2) / splitWidth;
        moveToScreen(toScreen);
    }

    /**
     * 滚动到下一屏
     */
    public void moveToNext() {
        moveToScreen(mCurScreen + 1);
    }

    /**
     * 滚动到上一屏
     */
    public void moveToPrevious() {
        moveToScreen(mCurScreen - 1);
    }

    /**
     * 测量容器宽
     * @param widthMeasureSpec widthMeasureSpec
     * @return width
     */
    private int measureWidth(int widthMeasureSpec) {
        int width;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalStateException("Must not be MeasureSpec.AT_MOST.");
        } else {
            width = size;
        }
        return width * getChildCount();
    }

    /**
     * 测量容器高度
     * @param heightMeasureSpec heightMeasureSpec
     * @return height
     */
    private int measureHeight(int heightMeasureSpec) {
        int height;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalStateException("Must not be MeasureSpec.AT_MOST");
        } else {
            height = size;
        }
        return height;
    }


}
