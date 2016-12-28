package com.david.study.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import com.david.study.R;

/**
 * 侧边栏菜单
 * Created by DavidChen on 2016/7/21.
 */
public class SliderMenu extends ViewGroup {
    private static final int DO_MOVING = 0x001;
    private static final int NOT_MOVING = 0x002;
    private int mMoving = NOT_MOVING;//是否可以滑动，默认不可滑动
    private static final int FLAG_SEPARATOR = 0x1;//标记变量，是否有分割线，占用最后一位
    private static final int FLAG_IS_OPEN = FLAG_SEPARATOR << 1;//标记变量，是否已打开，占用倒数第二位
    private int mFlags = FLAG_SEPARATOR >> 1;
    private static final int DEFAULT_SLIDING_WIDTH = 150;//dp
    private static final int DEFAULT_SEPARATOR = 1;//dp
    private static final int DEFAULT_TOUCH_WIDTH = 50;//dp

    private int mSlidingWidth;//侧边栏宽度
    private float mSeparator;//分割线宽度
    private int mTouchWidth;//感应宽度
    private int mScreenWidth;//屏幕宽度
    private Paint mPaint;
    private int mPreX,mFirstX;
    private Scroller mScroller;

    public SliderMenu(Context context) {
        this(context, null);
    }

    public SliderMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SliderMenu);
        mSlidingWidth = ta.getDimensionPixelSize(R.styleable.SliderMenu_sliding_width,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_SLIDING_WIDTH, getResources().getDisplayMetrics()));
        mSeparator = ta.getDimensionPixelSize(R.styleable.SliderMenu_separator,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_SEPARATOR, getResources().getDisplayMetrics()));
        mTouchWidth = ta.getDimensionPixelSize(R.styleable.SliderMenu_touch_width,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_TOUCH_WIDTH, getResources().getDisplayMetrics()));
        ta.recycle();
        if (mSeparator > 0) {
            mFlags = mFlags | FLAG_SEPARATOR;
        }
        mScreenWidth = getScreenWidth(context);
        setBackgroundColor(Color.alpha(255));//背景透明
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(mSeparator);
        mScroller = new Scroller(context);
    }

    /**
     * 获取屏幕宽度
     * @param context context
     * @return 屏幕宽度
     */
    private int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menu = getChildAt(0);
        menu.layout(0, 0, mSlidingWidth, getMeasuredHeight());
        View content = getChildAt(1);
        content.layout((int) (mSlidingWidth + mSeparator), 0, getMeasuredWidth(), getMeasuredHeight());
        this.scrollTo(mSlidingWidth, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量容器高度，默认不允许wrap_content
     * @param heightMeasureSpec heightMeasureSpec
     * @return 容器高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalStateException("layout_height can not be wrap_content");
        }
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        }
        return height;
    }

    /**
     * 测量容器宽度，默认不允许wrap_content
     * @param widthMeasureSpec widthMeasureSpec
     * @return 容器宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int width;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            throw new IllegalStateException("layout_height can not be wrap_content");
        }
        width = (int) (mScreenWidth + mSeparator + mSlidingWidth);
        return width;
    }

    /**
     * 子View不能超过2
     * @param child child
     * @param index index
     * @param params params
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() > 2) {
            throw new ArrayIndexOutOfBoundsException("Children count can't be more than 2.");
        }
    }



    /**
     * 对于ViewGroup大部分时候不需要重写onDraw，但是，这里需要绘制分割线
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mFlags & FLAG_SEPARATOR) == FLAG_SEPARATOR) {
            int left = (int) (mSlidingWidth + mSeparator / 2);
            canvas.drawLine(left, 0, left, getMeasuredHeight(), mPaint);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((mFlags & FLAG_IS_OPEN) == FLAG_IS_OPEN) {
                    mMoving = DO_MOVING;
                } else {
                    if (ev.getX() < mTouchWidth) {
                        mMoving = DO_MOVING;
                    } else {
                        mMoving = NOT_MOVING;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                mMoving = NOT_MOVING;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMoving == NOT_MOVING)
            return false;
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreX = x;
                mFirstX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mPreX;
                if (getScrollX() - dx >= 0 && getScrollX() - dx <= mSlidingWidth)
                    this.scrollBy(-dx, 0);
                mPreX = x;
                break;
            case MotionEvent.ACTION_UP:
                //判断移动
                dx = x - mFirstX;
                int remain = mSlidingWidth - getScrollX()/*Math.abs(dx)*/;
                //dx正为向右，负为向左
                boolean isOpen = (mFlags & FLAG_IS_OPEN) == FLAG_IS_OPEN;
                if (dx > 0 && !isOpen) {
                    //打开
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    mFlags = mFlags | FLAG_IS_OPEN;
                } else if (dx < 0 && isOpen) {
                    mScroller.startScroll(getScrollX(), 0, remain, 0);
                    mFlags = mFlags & ~FLAG_IS_OPEN;
                } else {
                    //校正，如：向右滑动又向左滑动
                    mScroller.startScroll(getScrollX(), 0, dx, 0);
                }
                invalidate();
                break;
        }
        return mMoving == DO_MOVING;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 关闭侧边栏
     */
    public void close() {
        if ((mFlags & FLAG_IS_OPEN) == FLAG_IS_OPEN) {
            mScroller.startScroll(0, 0, mSlidingWidth, 0);
            invalidate();
            mFlags = mFlags & ~FLAG_IS_OPEN;
        }
    }

    /**
     * 打开侧边栏
     */
    public void open() {
        if (! ((mFlags & FLAG_IS_OPEN) == FLAG_IS_OPEN)) {
            mScroller.startScroll(mSlidingWidth, 0, -mSlidingWidth, 0);
            invalidate();
            mFlags = mFlags | FLAG_IS_OPEN;
        }
    }

    /**
     * 打开/关闭侧边栏
     */
    public void toggle() {

        boolean isOpen = (mFlags & FLAG_IS_OPEN) == FLAG_IS_OPEN;
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

}
