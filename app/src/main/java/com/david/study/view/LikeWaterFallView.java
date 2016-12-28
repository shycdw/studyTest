package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * 仿照瀑布流控件，因为功能简单，且不支持大部分功能，所以，是仿照
 * Created by DavidChen on 2016/7/20.
 */
public class LikeWaterFallView extends ViewGroup {

    private static final int DEFAULT_SPACE = 5;
    private static final int DEFAULT_MAX_ROW = 4;

    private int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_SPACE, getResources().getDisplayMetrics());
    private int row = DEFAULT_MAX_ROW;

    public LikeWaterFallView(Context context) {
        super(context);
    }

    public LikeWaterFallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeWaterFallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int perHeight[] = new int[row];
        for (int i = 0; i < row; i++) {
            perHeight[i] = paddingTop;
        }
        int n = getChildCount();
        int perWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 3 * space) / 4;
        for (int i = 0; i < n; i++) {
            View child = getChildAt(i);
            int index = i % row;
            int left = getPaddingLeft() + index * perWidth + index * space;
            child.layout(left, perHeight[index], left + perWidth,
                    perHeight[index] + child.getMeasuredHeight());
            perHeight[index] = perHeight[index] + child.getMeasuredHeight() + space;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
