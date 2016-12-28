package com.david.study.view3;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * 第一级菜单固定的ExpandableListView
 * Created by DavidChen on 2016/10/25.
 * // 1、如何获取当前组的菜单内容 ----------> int firstVisiblePos = getFirstVisiblePosition();int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
 * // 2、何时绘制                 ----------> 通过dispatchDraw方法，我们绘制子元素，这个header，则是作为子元素来绘制即可
 * // 3、交替时绘制方式           ----------> 通过layout方法，不断的将view向上移动
 * // 4、view的点击事件           ----------> 通过判断是否落在header区域内，来判断是否点击
 */
public class PinnedHeaderExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener {

    private static final String TAG = "PinnedHeaderExpandableListView";

    public interface OnHeaderUpdateListener { // 用于监听header的变化
        /**
         * 采用单例模式获取同一个view对象
         * 注意：view必须要有LayoutParams
         */
        View getPinnedHeader();

        void updatePinnedHeader(int firstVisibleGroup);
    }

    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;
    private OnScrollListener mOnScrollListener;
    private OnHeaderUpdateListener mOnHeaderUpdateListener;

    public PinnedHeaderExpandableListView(Context context) {
        this(context, null);
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l != this) {
            mOnScrollListener = l;
        }
        super.setOnScrollListener(this);
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
        mOnHeaderUpdateListener = listener;
        if (listener == null) {
            return;
        }
        mHeaderView = listener.getPinnedHeader();
        int firstVisiblePos = getFirstVisiblePosition();
        int firstVisibleGroup = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));//根据首个可见的位置，获得所在组
        listener.updatePinnedHeader(firstVisibleGroup);
        requestLayout();//此处要重新布局，主要是要重新测量获得headerView的宽高
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView == null) {
            return;
        }
        measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeaderView == null) {
            return;
        }
        mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) { // view绘制过程：背景->自己->子View->装饰，此处用dispatchDraw绘制子view
        super.dispatchDraw(canvas);
        if (mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    private boolean mActionDownHappened = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//此处要判断点击所在区域
        int x= (int) ev.getX();
        int y = (int) ev.getY();
        int pos = pointToPosition(x, y);
        if (y > mHeaderView.getTop() && y < mHeaderView.getBottom()) { // 如果在此区域内
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mActionDownHappened = true;
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                int groupPosition = getPackedPositionGroup(getExpandableListPosition(pos));
                if (groupPosition != INVALID_POSITION && mActionDownHappened) { // 如果是点击事件
                    if (isGroupExpanded(groupPosition)) {
                        collapseGroup(groupPosition);
                    } else {
                        expandGroup(groupPosition);
                    }
                    mActionDownHappened = false;
                }
            }
            return true; // 已经分发
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 如果已经是静止状态，则重绘headerView
        if (mHeaderView != null && scrollState == SCROLL_STATE_IDLE) {
            int firstVisiblePos = getFirstVisiblePosition();
            if (firstVisiblePos == 0) { // 如果第一个可见项位置为0，则重新布局这个headerView
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0) {
            //重绘headerView
            refreshHeaderView();
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void refreshHeaderView() {
        if (mHeaderView == null) {
            return;
        }
        int firstVisiblePos = getFirstVisiblePosition();
        int nextPos = firstVisiblePos + 1;
        int firstVisibleGroup = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        int nextGroup = getPackedPositionGroup(getExpandableListPosition(nextPos));
        if (nextGroup == firstVisibleGroup + 1) {//如果下一个就是下一组，则重新layout HeaderView
            View view = getChildAt(1);
            if (view.getTop() <= mHeaderHeight) {
                int deltaY = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -deltaY, mHeaderWidth, mHeaderHeight - deltaY);
            }
        } else {
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }
        if (mOnHeaderUpdateListener != null) {
            mOnHeaderUpdateListener.updatePinnedHeader(firstVisibleGroup);
        }
    }
}
