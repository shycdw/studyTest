package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 流式布局
 * Created by DavidChen on 2016/7/19.
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int maxHeight = 0;
        int n = getChildCount();
        for (int i = 0; i < n; i ++) {
            View child = getChildAt(i);
            child.layout(left, top, left + child.getMeasuredWidth(), top + getMeasuredHeight());
            left += child.getMeasuredWidth();
            if (i < n - 1 && left + getChildAt(i + 1).getMeasuredWidth() + getPaddingRight()
                    > getMeasuredWidth()) {
                left = getPaddingLeft();
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
     * 测量容器height
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
            //wrap_content时，容器宽高度跟随内容
            int width = getMeasuredWidth();
            int n = getChildCount();
            int maxHeight = 0;//当前行子组件的最大高度
            int maxLineWidth = 0;//当前行子组件总宽度
            for (int i = 0; i < n; i ++) {
                View child = getChildAt(i);
                maxLineWidth += child.getMeasuredWidth();
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                //预测是否需要换行
                if (i < n - 1 && maxLineWidth + getChildAt(i + 1).getMeasuredWidth()
                        > width - getPaddingLeft() - getPaddingRight()) {
                    //当前子组件宽度超过容器宽度，且不是最后一个view，
                    // 则此时容器高度，加上当前行高，且要进行换行初始化
                    height += maxHeight;
                    maxLineWidth = 0;
                    maxHeight = 0;
                } else if (i == n - 1){
                    //如果当前组件已经是最后一个view，则容器高直接加上当前行的最大高度
                    height += maxHeight;
                }
            }
            height += getPaddingBottom() + getPaddingTop();
        }
        return height;
    }

    /**
     * 测量容器width
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
            //计算所有子组件所占宽
            int allChildWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                int childWidth = getChildAt(i).getMeasuredWidth();
                if (childWidth > size) throw new IllegalStateException("Sub view is too large!");
                allChildWidth += childWidth;
            }
            if (allChildWidth + getPaddingLeft() + getPaddingRight() > size) {
                width = size;
            } else {
                width = allChildWidth + getPaddingLeft() + getPaddingRight();
            }
        }
        return width;
    }
}
