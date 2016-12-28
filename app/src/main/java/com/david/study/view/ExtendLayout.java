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
 * 自定义的侧滑显示删除按钮控件
 * Created by DavidChen on 2016/7/22.
 */
public class ExtendLayout extends ViewGroup {
    private int mTvDeleteWidth;

    public ExtendLayout(Context context) {
        this(context, null);
    }

    public ExtendLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView tvDelete = createDeleteView();
        addView(tvDelete, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View tvDelete = getChildAt(0);
        View content = getChildAt(1);
        mTvDeleteWidth = tvDelete.getMeasuredWidth();
        tvDelete.layout(0, 0, mTvDeleteWidth, getMeasuredHeight());
        content.layout(mTvDeleteWidth, 0, getMeasuredWidth(), getMeasuredHeight());
        this.scrollTo(mTvDeleteWidth, 0);
    }

    /**
     * 返回自定义的删除按钮，为TextView
     * @return 红底白字的删除按钮
     */
    private TextView createDeleteView() {
        TextView tv = new TextView(getContext());
        tv.setText("删除");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setBackgroundColor(Color.RED);
        tv.setTextColor(Color.WHITE);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int px = (int) (10 * scale + 0.5f);
        tv.setPadding(px * 2, px, px * 2, px);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        tv.setClickable(true);
        return tv;
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        if (getChildCount() > 2) {
            throw new IndexOutOfBoundsException("Sub views is too many.");
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
     * 测量高度
     * @param heightMeasureSpec heightMeasureSpec
     * @return 高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            View child = getChildAt(1);
            if (child != null) {
                height = child.getMeasuredHeight();
            }
        }
        //此处重新调整删除按钮的高度，使之与内容区一样高
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
        View tv = getChildAt(0);
        if (tv != null) {
            tv.setLayoutParams(params);
        }
        return height;
    }

    /**
     * 测量宽度
     * @param widthMeasureSpec widthMeasureSpec
     * @return 宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            View content = getChildAt(1);
            if (content != null) {
                width = content.getMeasuredWidth();
            }
        }
        View child = getChildAt(0);
        if (child != null) {
            mTvDeleteWidth = child.getMeasuredWidth();
        }

        return width + mTvDeleteWidth;
    }

    public View getDeleteButton() {
        return getChildAt(0);
    }
}
