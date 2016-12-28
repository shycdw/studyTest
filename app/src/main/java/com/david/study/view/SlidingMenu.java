package com.david.study.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.david.study.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * 侧边栏，继承自HorizontalView
 * Created by DavidChen on 2016/7/21.
 */
public class SlidingMenu extends HorizontalScrollView {
    private static final int DEFAULT_RIGHT_PADDING = 50;

    private boolean mOnce = true;
    private int mRightPadding;
    private boolean isOpen = false;
    private int mMenuWith;
    private View mMenu;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.SlidingMenu);
        mRightPadding = ta.getDimensionPixelOffset(
                R.styleable.SlidingMenu_right_padding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_RIGHT_PADDING,
                        getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取HorizontalScrollView中的唯一的view，我们默认为LinearLayout
        LinearLayout subView = (LinearLayout) getChildAt(0);
        mMenu = subView.getChildAt(0);
        View content = subView.getChildAt(1);
        int mScreenWidth = getScreenWidth();
        mMenuWith = mScreenWidth - mRightPadding;
        mMenu.getLayoutParams().width = mMenuWith;
        content.getLayoutParams().width = mScreenWidth;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mOnce) {
            this.scrollTo(mMenuWith, 0);
            mOnce = false;
        }
    }

    /**
     * 滚动发生时调用
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //l为getScrollX（）随着滑动，1~0之间变化，刚刚打开时为1，完全打开时为0
        float scale = l * 1.0f / mMenuWith;
        ViewHelper.setTranslationX(mMenu, mMenuWith * scale);
    }

    /**
     * 获取屏幕宽度
     * @return 屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager manager =
                (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (getScrollX() > (mMenuWith) / 2) {
                //关闭
                smoothScrollTo(mMenuWith, 0);
                isOpen = false;
            } else {
                //打开
                smoothScrollTo(0, 0);
                isOpen = true;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void close() {
        if (isOpen) {
            smoothScrollTo(mMenuWith, 0);
        }
    }

    public void open() {
        if (!isOpen) {
            smoothScrollTo(0, 0);
        }
    }

    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }
}
