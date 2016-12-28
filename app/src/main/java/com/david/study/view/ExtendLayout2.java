package com.david.study.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 右侧滑item布局
 * Created by DavidChen on 2016/7/22.
 */
public class ExtendLayout2 extends ViewGroup {
    private int mTvWidth;
    public ExtendLayout2(Context context) {
        this(context, null);
    }

    public ExtendLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView tv = createDeleteButton();
        addView(tv, 1);
    }

    /**
     * 创建删除按钮
     */
    private TextView createDeleteButton() {
        TextView textView = new TextView(getContext());
        textView.setText("删除");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.RED);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int px = (int) (10 * scale + 0.5f);
        textView.setPadding(px * 2, px, px * 2, px);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setClickable(true);
        return textView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {

        }
        return 0;
    }

    /**
     * 测量容器宽度
     * @param widthMeasureSpec widthMeasureSpec
     * @return 容器宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED){
            View content = getChildAt(0);
            if (content != null) {
                width = content.getMeasuredWidth();
            }
            View tv = getChildAt(1);
            if (tv != null) {
                mTvWidth = tv.getWidth();
            }
        }
        return width + mTvWidth;
    }
}
