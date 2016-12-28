package com.david.study.company;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.study.R;

/**
 * 标题View
 * Created by DavidChen on 2016/9/5.
 */
public class SimpleTabStripView extends LinearLayout {
    private static final String TAG = "SimpleTabStripView";
    private static final String[] DEFAULT_TITLES = new String[]{"Tab1", "Tab2", "Tab3"};
    private static final int DEFAULT_VISIBLE = 3;//默认可见三个tab
    private static final int DEFAULT_SELECTED_COLOR = Color.RED;//默认的选中tab和下划线相同颜色
    private static final int DEFAULT_UNSELECTED_COLOR = Color.GRAY;//默认下划线颜色
    private static final int DEFAULT_UNDERLINE_HEIGHT = 20;//px,默认下划线高度
    private static final int DEFAULT_TXT_SIZE = 16;//sp，默认字体大小

    private int mSelectedTxtColor;  //选中文本颜色
    private CharSequence[] mTitles; //tab标题
    private int mWidth; //控件宽度
    private int mVisibleTabsCount;  //可见tab数量
    private int mTabWidth; //tab宽度
    private ViewPager mViewPager;
    private int mTranslationX;  //偏移量
    private int mUnderlineColor;    //下划线颜色
    private int mUnselectedTxtColor;    //未选中文本颜色
    private int mSelectedPosition = 0;//此处主要是为了标记当前选中的tab，因为设置viewpager时不知道此时view是否已经添加，所以只能先做标记
    private int mUnderlineHeight;   //下划线高度
    private UnderlineStyle mUnderlineStyle; //下划线样式（已经定义好了条形和三角形的样式，扩展可实现UnderlineStyle接口）
    private int mTabTextSize;   //文字颜色

    public SimpleTabStripView(Context context) {
        this(context, null);
    }

    public SimpleTabStripView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTabStripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleTabStripView);
        mTitles = ta.getTextArray(R.styleable.SimpleTabStripView_titles);
        if (mTitles == null) {
            mTitles = DEFAULT_TITLES;
        }
        mVisibleTabsCount = ta.getInt(R.styleable.SimpleTabStripView_visible_tabs_count, DEFAULT_VISIBLE);
        mSelectedTxtColor = ta.getColor(R.styleable.SimpleTabStripView_selected_txt_color, DEFAULT_SELECTED_COLOR);
        mUnderlineColor = ta.getColor(R.styleable.SimpleTabStripView_underline_color, mSelectedTxtColor);
        mUnselectedTxtColor = ta.getColor(R.styleable.SimpleTabStripView_unselected_txt_color, DEFAULT_UNSELECTED_COLOR);
        mUnderlineHeight = ta.getDimensionPixelSize(R.styleable.SimpleTabStripView_underline_height, DEFAULT_UNDERLINE_HEIGHT);
        mTabTextSize = (int) ta.getDimension(R.styleable.SimpleTabStripView_tab_txt_size, sp2px(context, DEFAULT_TXT_SIZE));
        ta.recycle();
        mUnderlineStyle = new RectUnderlineStyle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mTabWidth = w / mVisibleTabsCount;
        updateTabs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mUnderlineStyle != null) {
            mUnderlineStyle.draw(canvas, mTranslationX, mTabWidth, mUnderlineColor, getMeasuredHeight(), mUnderlineHeight);
        }
    }

    /**
     * 创建tab
     */
    private void updateTabs() {
        removeAllViews();
        for (int i = 0; i < mTitles.length; i++) {
            final int j = i;
            CharSequence title = mTitles[i];
            TextView tab = new TextView(getContext());
            tab.setLayoutParams(new ViewGroup.LayoutParams(mTabWidth, getMeasuredHeight()));
            tab.setText(title);
            tab.setTextColor(i == mSelectedPosition ? mSelectedTxtColor : mUnselectedTxtColor);
            tab.setTextSize(px2sp(getContext(), mTabTextSize));
            tab.setGravity(Gravity.CENTER);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
            addView(tab);
        }
    }

    public void setViewPager(ViewPager viewpager) {
        this.mViewPager = viewpager;
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollToChild(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                highLightTextView(position);
                mSelectedPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mSelectedPosition);
        //此处不hightLight文本，主要是因为，set的时候不能确定宽度是否已经测量。
    }

    /**
     * 滚动到指定位置
     *
     * @param position       起始位置
     * @param positionOffset 偏移量
     */
    private void scrollToChild(int position, float positionOffset) {
        mTranslationX = (int) ((position + positionOffset) * mTabWidth);
        invalidate();
        if (position >= mVisibleTabsCount - 2 && getChildCount() > mVisibleTabsCount && position < getChildCount() - 2) {
            if (mVisibleTabsCount != 1) {
                //例：可见3个，当前在1位置，由1滚动到2，
                // 则position为1，而滚动的距离则为positionOffset * mTabWidth
                scrollTo((int) ((position + 2 - mVisibleTabsCount + positionOffset) * mTabWidth), 0);
            } else {
                scrollTo((int) ((position + positionOffset) * mVisibleTabsCount), 0);
            }
        }
    }

    /**
     * 重置所有tab的字体颜色
     */
    private void resetAllTabsColor() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View tab = getChildAt(i);
            if (tab instanceof TextView) {
                ((TextView) tab).setTextColor(mUnselectedTxtColor);
            }
        }
    }

    /**
     * 高亮文本
     *
     * @param pos 选中的tab位置
     */
    private void highLightTextView(int pos) {
        resetAllTabsColor();
        View tab = getChildAt(pos);
        if (tab instanceof TextView) {
            ((TextView) tab).setTextColor(mSelectedTxtColor);
        }
    }

    /**
     * 设置标题
     *
     * @param titles 所有tab标题
     */
    public void setTitles(CharSequence[] titles) {
        this.mTitles = titles;
        updateTabs();
    }

    /**
     * 设置选中颜色
     *
     * @param selectedTxtColor 选中的tab文字颜色
     */
    public void setSelectedTxtColor(int selectedTxtColor) {
        this.mSelectedTxtColor = selectedTxtColor;
        highLightTextView(mSelectedPosition);
    }

    /**
     * 设置未选中文本颜色
     *
     * @param unselectedTxtColor 未选中的tab文字颜色
     */
    public void setUnselectedTxtColor(int unselectedTxtColor) {
        this.mUnselectedTxtColor = unselectedTxtColor;
        highLightTextView(mSelectedPosition);
    }

    public void setUnderLineStyle(UnderlineStyle underLineStyle) {
        this.mUnderlineStyle = underLineStyle;
    }

    /**
     * 应对不同的底色
     */
    public interface UnderlineStyle {
        /**
         * 绘制选中的按钮的下划线样式（三角形，长方形等等）
         * @param canvas canvas
         * @param translationX X偏移量
         * @param tabWidth 每个tab宽度
         * @param underlineColor 下划线颜色
         * @param height tab高度
         * @param underlineHeight 下划线高度（xml设置）
         */
        void draw(Canvas canvas, int translationX, int tabWidth, int underlineColor, int height, int underlineHeight);
    }

    /**
     * 设置可见数
     * @param visibleTabsCount 可见tab数量
     */
    public void setVisibleTabsCount(int visibleTabsCount) {
        this.mVisibleTabsCount = visibleTabsCount;
        mTabWidth = mWidth / mVisibleTabsCount;
        updateTabs();
    }

    /**
     * sp转px
     * @param context 上下文
     * @param spVal sp值
     * @return px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     * @param context 上下文
     * @param pxVal px值
     * @return sp值
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
