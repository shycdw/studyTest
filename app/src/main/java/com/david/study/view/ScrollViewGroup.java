package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;

/**
 * 测试scroller类
 * Created by DavidChen on 2016/7/20.
 */
public class ScrollViewGroup extends ViewGroup {

    private Scroller mScroller;
    private Button mBtn;

    public ScrollViewGroup(Context context) {
        this(context, null);
    }

    public ScrollViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mBtn = new Button(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mBtn.setText("hello");
        this.addView(mBtn, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mBtn.layout(10, 10, mBtn.getMeasuredWidth() + 10, mBtn.getMeasuredHeight() + 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     * 实现平滑滚动。
     * 重写原因：当调用invalidate()或postInvalidate()方法，将重绘请求发送到ViewRoot，
     * 再分发到对应组件，调用draw方法，draw(canvas)-->dispatchDraw(canvas)
     * -->drawChild(canvas, child, drawingTime)，在最后这个方法中会有child.computeScroll();的调用，
     * 但是该方法是一个空方法，需要重写该方法才能实现平滑滚动。
     *
     * PS:每次调用重绘方法，就会进行draw操作，在draw操作里会调用computeScroll()方法，这个方法正常是
     * 空的。当我们需要滚动效果的时候，需要重写这个方法，在此方法中判断，是否滚动完成，
     * 未完成则调用scrollTo()方法，之后再次调用重绘，如此循环，直到滚动完成。（当然也可以空值子View的滚动）
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //设置容器内组件的位置
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //重绘以刷新产生动画
            postInvalidate();
        }
    }

    /**
     * 开始滚动，用于外部调用
     */
    public void start() {
        //从当前位置开始滚动，x方向向右滚动900
        //y方向不变，也就是水平滚动
        mScroller.startScroll(this.getScrollX(), this.getScrollY(), -900, 0, 10000);
        //重绘
        postInvalidate();
    }

    /**
     * 取消滚动，直接到达目的地
     */
    public void abort() {
        mScroller.abortAnimation();
    }
}
