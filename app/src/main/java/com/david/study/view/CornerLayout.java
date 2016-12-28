package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义四角显示控件ViewGroup
 * Created by DavidChen on 2016/7/19.
 */
public class CornerLayout extends ViewGroup {
    public CornerLayout(Context context) {
        super(context);
    }

    public CornerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CornerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftPadding = getPaddingLeft();
        int topPadding = getPaddingTop();
        int rightPadding = getPaddingRight();
        int bottomPadding = getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            switch (i) {
                case 0://定位到左上角
                    child.layout(leftPadding, topPadding,
                            child.getMeasuredWidth() + leftPadding,
                            child.getMeasuredHeight() + topPadding);
                    break;
                case 1://定位到右上角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth() - rightPadding,
                            topPadding, getMeasuredWidth() - rightPadding,
                            child.getMeasuredHeight() + topPadding);
                    break;
                case 2://定位到右下角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth() - rightPadding,
                            getMeasuredHeight() - child.getMeasuredHeight() - bottomPadding,
                            getMeasuredWidth() - rightPadding, getMeasuredHeight() - bottomPadding);
                    break;
                case 3://定位到左下角
                    child.layout(leftPadding,
                            getMeasuredHeight() - child.getMeasuredHeight() - bottomPadding,
                            child.getMeasuredWidth() + leftPadding,
                            getMeasuredHeight() - bottomPadding);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量子组件大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量自己大小
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        //应用尺寸
        setMeasuredDimension(width, height);
    }

    /**
     * 测量容器高度
     *
     * @param heightMeasureSpec heightMeasureSpec
     * @return 容器高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//match_parent或具体值
            height = size;
        } else if (mode == MeasureSpec.AT_MOST) {//wrap_content
            int aHeight = 0;
            int bHeight = 0;
            int cHeight = 0;
            int dHeight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                //此处做if判断，是防止没有足够的组件（4个），防止下标越界
                if (i == 0) {
                    aHeight = getChildAt(i).getMeasuredHeight();
                } else if (i == 1) {
                    bHeight = getChildAt(i).getMeasuredHeight();
                } else if (i == 2) {
                    cHeight = getChildAt(i).getMeasuredHeight();
                } else if (i == 3) {
                    dHeight = getChildAt(i).getMeasuredHeight();
                }
            }
            height = Math.max(aHeight, bHeight) + Math.max(cHeight, dHeight)
                    + getPaddingTop() + getPaddingBottom();
        }
        return height;
    }

    /**
     * 测量容器宽度
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return 容器宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//match_parent或具体值
            width = size;
        } else if (mode == MeasureSpec.AT_MOST) {//wrap_content
            int aWidth = 0;
            int bWidth = 0;
            int cWidth = 0;
            int dWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                //此处做if判断，是防止没有足够的组件（4个），防止下标越界
                if (i == 0) {
                    aWidth = getChildAt(i).getMeasuredWidth();
                } else if (i == 1) {
                    bWidth = getChildAt(i).getMeasuredWidth();
                } else if (i == 2) {
                    cWidth = getChildAt(i).getMeasuredWidth();
                } else if (i == 3) {
                    dWidth = getChildAt(i).getMeasuredWidth();
                }
            }
            width = Math.max(aWidth, dWidth) + Math.max(bWidth, cWidth)
                    + getPaddingLeft() + getPaddingRight();
        }
        return width;
    }
}
