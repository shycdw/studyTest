package com.david.study;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 添加之后根据宽度自动换行的layout。
 * 注意：*此处根据业务需求将子View全部写成TextView,之后若是想改别的再修改，
 * 但是不用写那么多，把onMeasure，onLayout留下即可，只更改增加view的方法
 * <p>
 * Created by DavidChen on 2016/10/27.
 */
public class FixedLayout extends ViewGroup {

    private static final String TAG = "FixedLayout";
    private static final int DEFAULT_SPACING = 0;

    private int mLineSpacing;   // px,行间距
    private int mColumnSpacing; // px,列间距

    private List<String> mItems;    // 内容

    private String mSelectedContent = "";    //选中的值

    public FixedLayout(Context context) {
        this(context, null);
    }

    public FixedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.FixedLayout);
        mLineSpacing = ta.getDimensionPixelOffset(R.styleable.FixedLayout_line_spacing, DEFAULT_SPACING);
        mColumnSpacing = ta.getDimensionPixelOffset(R.styleable.FixedLayout_column_spacing, DEFAULT_SPACING);
        ta.recycle();
    }
    //---------------------------------------华丽的分割线-----------------------------------------//
    // 请在此华丽的分割线内完成自定义的内容，若是以后想写别的，可以考虑用策略模式实现

    public String getSelectedContent() {
        return mSelectedContent;
    }

    public void setItem(List<String> items) {
        this.mItems = items;
        updateItem();
        setChecked(0);
    }

    /**
     * 设置某个位置的item为选中状态
     * @param index list中的位置
     */
    private void setChecked(int index) {
        if (mItems != null && index < mItems.size()) {
            restoreAllItems();
            checked(index);
            mSelectedContent = mItems.get(index);
        }
    }

    private void checked(int index) {
        View view = getChildAt(index);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.parseColor("#FFFB034F"));
            view.setBackgroundResource(R.drawable.shape_bg_red_border);
        }
    }

    private void restoreAllItems() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.parseColor("#DE000000"));
                view.setBackgroundResource(R.drawable.shape_bg_white_border_gray);
            }
        }
    }

    /**
     * 重置所有item
     */
    private void updateItem() {
        removeAllViews();
        if (mItems != null) {
            int count = mItems.size();
            for (int i = 0; i < count; i++) {
                final String content = mItems.get(i);
                TextView textView = getTextView(content);
                final int temp = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedContent = content;
                        setChecked(temp);
                    }
                });
                addView(textView);
            }
        }
    }

    /**
     * 获取TextView
     *
     * @param content text
     */
    private TextView getTextView(String content) {
        int padding = dp2px(getContext(), 16);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView.setPadding(padding, padding, padding, padding);
        textView.setBackgroundResource(R.drawable.shape_bg_red_border);
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#DE000000"));
        textView.setText(content);
        return textView;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpVal   dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    //---------------------------------------华丽的分割线-----------------------------------------//

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 计算高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        int width = getMeasuredWidth();
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            // 此处要根据view的宽度来计算高度
            int childCount = getChildCount();
            int lineWidth = 0;  // 此用来记录当前行宽度，用来判断是否要换行
            int totalHeight = 0;    // 当前行高度
            int maxHeight = 0;  // 每行最大宽度
            // 原则：只计算当前行宽高，以及下面的行间距（要在换行时计算）。
            // 这里有个细节考虑：如果每次换行都多一个下面的行间距出来，则最后一个view测量完分两种情况：
            // （1）没有满一行（包括第一行），不换行则最后一行下面不会计算多的行间距；
            // （2）如果满一行，但是这一行只有一个超出宽度的唯一子View，则会换行，换行则会加一个行间距
            // 处理：在最后判断最后一行的lineWidth，如果已经有了子View，则不用去掉行间距，如果没有，则去掉行间距，当然还要考虑到没有子View不用去掉
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                //  先计算是否超过最长宽度: 当前行内容宽度（view宽度和之间的列宽） + padding > width
                int currentWidth = lineWidth + view.getMeasuredWidth();
                if (lineWidth > 0) {
                    currentWidth += mColumnSpacing;
                }
                currentWidth = currentWidth + getPaddingRight() + getPaddingLeft();  // 加上padding
                if (currentWidth > width) { // 如果超过了,则换行
                    if (lineWidth <= 0) {   // 若当前总宽lineWidth为0（当前行没有控件），则当前子View不换行，且高度直接加上当前子View的高度，重置下行数据为0
                        totalHeight += view.getMeasuredHeight();
                        lineWidth = 0;
                        maxHeight = 0;
                    } else {    // 若当前总宽lineWidth大于0（已经有了控件），则当前子View换行，且高度加上之前最大值，且要重置为当前子View数据
                        totalHeight += maxHeight;
                        lineWidth = view.getMeasuredWidth();
                        maxHeight = view.getMeasuredHeight();
                    }
                    //加上换行后的行间距
                    totalHeight += mLineSpacing;
                } else {    // 如果没超过，则不换行
                    if (maxHeight < view.getMeasuredHeight()) {
                        maxHeight = view.getMeasuredHeight();
                    }
                    if (lineWidth > 0) {  // 此处根据当前行宽是否大于0，true则已经有列，可以加上列间距,false则当前子View为第一列，不用加上列间距
                        lineWidth += mColumnSpacing;
                    }
                    lineWidth += view.getMeasuredWidth();
                }
                if (i == childCount - 1) {  //最后一行单独处理
                    // 当最后一行没有View时，去掉上一行多的一个行间距
                    if (lineWidth <= 0) {
                        totalHeight -= mLineSpacing;
                    }
                    //加列高（此处不用判断是有view，没有时为0，有时为最大值，都可以）
                    totalHeight += maxHeight;
                }
            }
            // 加上padding
            height = totalHeight + getPaddingTop() + getPaddingBottom();
        }
        return height;
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return width
     */
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (mode == MeasureSpec.EXACTLY) {  // 确定值（或MATCH_PARENT），则使用确定值
            width = size;
        } else if (mode == MeasureSpec.AT_MOST) {    // WRAP_CONTENT,
            // 先计算所有子View的宽度和，包括padding和列间距，如果和大于控件最大宽度（size），则取size
            // 先计算所有view宽度和
            int childCount = getChildCount();
            int totalWidth = 0;
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                totalWidth += view.getMeasuredWidth();
            }
            //加上padding
            totalWidth = totalWidth + getPaddingLeft() + getPaddingRight();
            // 再加上列间距
            if (childCount > 1) {
                totalWidth = totalWidth + (childCount - 1) * mColumnSpacing;
            }
            //判断是否大于最大宽度
            width = totalWidth > size ? size : totalWidth;
        }
        return width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int width = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int currentWidth = paddingLeft; // 当前行宽（包括paddingLeft,columnsSpacing,view宽）
        int currentHeight = paddingTop;   // 当前行高（包括paddingTop,lineSpacing,view高）
        int maxHeight = 0;  // 每行最大view高度
        int left, top, right, bottom;
        for (int i = 0; i < childCount; i++) {  // 此处可参考onMeasure的逻辑，略有不同
            View view = getChildAt(i);
            // 计算包含了当前子view的行宽
            int destWidth = currentWidth;
            if (currentWidth > paddingLeft) {  // 当前行有view，则加列间距
                destWidth += mColumnSpacing;
            }
            destWidth += view.getMeasuredWidth();
            destWidth += paddingRight;  // 加上paddingRight
            if (destWidth > width) {   // 如果大于最大行宽,则换行重新计算

                if (currentWidth <= paddingLeft) { // 如果当前行没有view,则不换行，且下一行数据置空
                    // 获取子View布局位置
                    left = currentWidth;
                    top = currentHeight; // 此处为何先减掉行间距，因为在判断到换行时已经加了本行的行间距，所以要先减掉
                    // 重置数据为0
                    currentHeight += view.getMeasuredHeight();
                    maxHeight = 0;
                    currentWidth = paddingLeft;
                } else {    // 当前行已经有了view，则view要以换行之后来布局
                    // 重置下一行数据为当前子view的数据
                    currentHeight += maxHeight;
                    currentWidth = paddingLeft + view.getMeasuredWidth();
                    maxHeight = view.getMeasuredHeight();
                    left = paddingLeft;
                    top = currentHeight + mLineSpacing;
                }
                // 换行后，加上前一行行下面的行间距
                currentHeight += mLineSpacing;
            } else {    // 不大于最大行宽增加到当前行
                if (maxHeight < view.getMeasuredHeight()) { // 更新最大行高
                    maxHeight = view.getMeasuredHeight();
                }
                if (currentWidth > paddingLeft) { // 增加列间距
                    currentWidth += mColumnSpacing;
                }
                // 获取最终位置
                left = currentWidth;
                top = currentHeight;
                currentWidth += view.getMeasuredWidth();
            }
            // 最后则不用像measure那样去掉间距，因为只是布局，只要计算每个view位置即可
            right = left + view.getMeasuredWidth();
            bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
        }
    }
}
