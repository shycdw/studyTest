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
 * 左侧边栏
 * Created by DavidChen on 2016/7/20.
 */
public class LeftSlideView extends ViewGroup {
    private static final int TOUCH_STATE_STOP = 0x001;
    private static final int TOUCH_STATE_FLING = 0x002;
    private static final int DEFAULT_SLOP = 2;//dp
    private static final int SNAP_VELOCITY = 400;
    private static final float DEFAULT_SCALED = 0.8f;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop = 0; //最小滑动距离，超过了才认为开始滑动
    private float lastMotionX = 0;//上次触摸位置
    private float mScaled = DEFAULT_SCALED;
    private int mTouchState = TOUCH_STATE_STOP;
    private boolean isMenuOpen = false;

    public LeftSlideView(Context context) {
        this(context, null);
    }

    public LeftSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller  = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量容器height
     * @param heightMeasureSpec heightMeasureSpec
     * @return height
     */
    private int measureHeight(int heightMeasureSpec) {
        int height;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalStateException("Must not be MeasureSpec.AT_MOST.");
        } else {
            height = size;
        }
        return height;
    }

    /**
     * 测量容器width
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
            width = (int) (mScaled * size) + size;
        }
        return width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() < 2) {
            throw new NullPointerException("Only support 2 sub views.");
        }
        View menu = getChildAt(0);
        View content = getChildAt(1);
        int perWidth = (int) (getMeasuredWidth() / (1 + mScaled));
        content.layout((int) (mScaled * perWidth), 0, getMeasuredWidth(), getMeasuredHeight());
        menu.layout(0, 0, (int) (mScaled * perWidth), getMeasuredHeight());
        //默认出现container
        this.scrollTo((int) (mScaled * perWidth), 0);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()) {
            //设置容器内组件新位置
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //重绘产生动画效果
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        final int x = (int) ev.getX();
        //如果手指正在滑动，则停止分发
        if (action == MotionEvent.ACTION_MOVE && mTouchState == TOUCH_STATE_FLING)
            return true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //如果正在滑动，则停止滑动
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_STOP : TOUCH_STATE_FLING;
                lastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                //如果大于最小滑动距离，则为滑动
                int dx = (int) (x - lastMotionX);
                if (dx > mTouchSlop) {
                    mTouchState = TOUCH_STATE_FLING;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_STOP;
                break;
        }
        return mTouchState != TOUCH_STATE_STOP;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        super.onTouchEvent(event);
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果按下手指时正在滚动，则立即停止
                if (mScroller != null && mTouchState == TOUCH_STATE_FLING) {
                    mScroller.abortAnimation();
                }
                lastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                //随手指滑动
                int dx = (int) (lastMotionX - x);
                if (getScrollX() + dx > 0
                        && getScrollX() + dx < getMeasuredWidth() / (1 + mScaled) * mScaled)
                    scrollBy(dx, 0);
                lastMotionX = x;
                break;
            case MotionEvent.ACTION_UP:
                //判断滑动
                final VelocityTracker tracker = mVelocityTracker;
                tracker.computeCurrentVelocity(1000);
                int velocityX = (int) tracker.getXVelocity();
                //根据x正负决定滑动方向，正向右，负向左
                if (velocityX > SNAP_VELOCITY) {
                    if (!isMenuOpen) {
                        openMenu();
                    }
                } else if (velocityX < -SNAP_VELOCITY) {
                    if (isMenuOpen) {
                        closeMenu();
                    }
                } else {
                    moveToDestination();
                }

                //释放并回收资源
                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mTouchState = TOUCH_STATE_STOP;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_STOP;
                break;
        }

        return true;
    }

    /**
     * 移动到menu
     */
    public void openMenu() {
        int scrollX = getScrollX();
        //总共需要移动的距离 - 已经移动的距离，则为，目标距离
        int dx = - scrollX;
        //开始移动
        mScroller.startScroll(scrollX, 0, dx, 0, Math.abs(dx));
        isMenuOpen = true;
        invalidate();
    }

    /**
     * 移动到内容区
     */
    public void closeMenu() {
        int scrollX = getScrollX();
        int perWidth = (int) (getMeasuredWidth() / (1 + mScaled));
        //总共需要移动的距离 - 已经移动的距离，则为，目标距离
        int dx = (int) (mScaled * perWidth - scrollX);
        //开始移动
        mScroller.startScroll(scrollX, 0, dx, 0, Math.abs(dx));
        isMenuOpen = false;
        invalidate();
    }

    /**
     * 根据移动判断滑动到哪个区域
     */
    private void moveToDestination() {
        //没一屏的宽度
        int perWidth = (int) (getMeasuredWidth() / (1 + mScaled));
        //判断移动方向
        if (getScrollX() <= 0.4 * perWidth) {
            openMenu();
        } else {
            closeMenu();
        }
    }

    /**
     * 公开方法
     */
    public void toggle() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
