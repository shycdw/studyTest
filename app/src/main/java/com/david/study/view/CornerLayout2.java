package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 测试子控件带有margin属性的ViewGroup
 * Created by DavidChen on 2016/7/19.
 */
public class CornerLayout2 extends ViewGroup {
    public CornerLayout2(Context context) {
        super(context);
    }

    public CornerLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CornerLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
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
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int leftMargin = params.leftMargin;
            int topMargin = params.topMargin;
            int rightMargin = params.rightMargin;
            int bottomMargin = params.bottomMargin;
            switch (i) {
                case 0://定位到左上角
                    child.layout(leftPadding + leftMargin, topPadding + topMargin,
                            child.getMeasuredWidth() + leftPadding + leftMargin,
                            child.getMeasuredHeight() + topPadding + topMargin);
                    break;
                case 1://定位到右上角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth() - rightPadding - rightMargin,
                            topPadding + topMargin, getMeasuredWidth() - rightPadding - rightMargin,
                            child.getMeasuredHeight() + topPadding + topMargin);
                    break;
                case 2://定位到右下角
                    child.layout(getMeasuredWidth() - child.getMeasuredWidth() - rightPadding - rightMargin,
                            getMeasuredHeight() - child.getMeasuredHeight() - bottomPadding - bottomMargin,
                            getMeasuredWidth() - rightPadding - rightMargin, getMeasuredHeight() - bottomPadding - bottomMargin);
                    break;
                case 3://定位到左下角
                    child.layout(leftPadding + leftMargin,
                            getMeasuredHeight() - child.getMeasuredHeight() - bottomPadding - bottomMargin,
                            child.getMeasuredWidth() + leftPadding + leftMargin,
                            getMeasuredHeight() - bottomPadding - bottomMargin);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量height
     *
     * @param heightMeasureSpec heightMeasureSpec
     * @return height
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            int aHeight, bHeight, cHeight, dHeight;
            aHeight = bHeight = cHeight = dHeight = 0;
            int marginVa, marginVb, marginVc, marginVd;
            marginVa = marginVb = marginVc = marginVd = 0;
            for (int i = 0; i < getChildCount(); i++) {
                MarginLayoutParams params = (MarginLayoutParams) getChildAt(i).getLayoutParams();
                if (i == 0) {
                    aHeight = getChildAt(i).getMeasuredHeight();
                    marginVa += params.topMargin + params.bottomMargin;
                    aHeight += marginVa;
                } else if (i == 1) {
                    bHeight = getChildAt(i).getMeasuredHeight();
                    marginVb += params.topMargin + params.bottomMargin;
                    bHeight += marginVb;
                } else if (i == 2) {
                    cHeight = getChildAt(i).getMeasuredHeight();
                    marginVc += params.topMargin + params.bottomMargin;
                    cHeight += marginVc;
                } else if (i == 3) {
                    dHeight = getChildAt(i).getMeasuredHeight();
                    marginVd += params.topMargin + params.bottomMargin;
                    dHeight += marginVd;
                }
                height = Math.max(aHeight, bHeight) + Math.max(cHeight, dHeight) + getPaddingTop()
                        + getPaddingBottom();
            }
        }
        return height;
    }

    /**
     * 测量width
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return width
     */
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            int aWidth, bWidth, cWidth, dWidth;
            aWidth = bWidth = cWidth = dWidth = 0;
            int marginHa, marginHb, marginHc, marginHd;
            marginHa = marginHb = marginHc = marginHd = 0;
            for (int i = 0; i < getChildCount(); i++) {
                MarginLayoutParams params = (MarginLayoutParams) getChildAt(i).getLayoutParams();
                if (i == 0) {
                    aWidth = getChildAt(i).getMeasuredWidth();
                    marginHa += params.leftMargin + params.rightMargin;
                    aWidth += marginHa;
                } else if (i == 1) {
                    bWidth = getChildAt(i).getMeasuredWidth();
                    marginHb += params.leftMargin + params.rightMargin;
                    bWidth += marginHb;
                } else if (i == 2) {
                    cWidth = getChildAt(i).getMeasuredWidth();
                    marginHc += params.leftMargin + params.rightMargin;
                    cWidth += marginHc;
                } else if (i == 3) {
                    dWidth = getChildAt(i).getMeasuredWidth();
                    marginHd += params.leftMargin + params.rightMargin;
                    dWidth += marginHd;
                }
                width = Math.max(aWidth, dWidth) + Math.max(bWidth, cWidth) + getPaddingLeft()
                        + getPaddingRight();
            }
        }
        return width;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
