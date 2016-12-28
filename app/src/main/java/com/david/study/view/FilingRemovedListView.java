package com.david.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 侧滑出现删除按钮的ListView
 * Created by DavidChen on 2016/7/22.
 */
public class FilingRemovedListView extends ListView {
    private static final int SNAP_VELOCITY = 600;
    private static final int SLIDE_MASK = 0x1;//是否可以滑动
    private static final int DELETE_VISIBLE_MASK = SLIDE_MASK << 1;//是否出现滑动按钮
    private int mFlags;//标识位
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private float mPreX;//前一个触摸点x
    private float mFirstX, mFirstY;//按下时的点
    private ExtendLayout mExtendLayout;//要滑动的列表项view
    private int mPosition = INVALID_POSITION;//要滑动的列表项index
    private int mTouchSlop;//最小滑动距离
    private OnRemovedItemListener mOnRemovedItemListener;

    public FilingRemovedListView(Context context) {
        this(context, null);
    }

    public FilingRemovedListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilingRemovedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    return super.dispatchTouchEvent(ev);
                }
                mFirstX = mPreX = x;
                mFirstY = y;
                mPosition = this.pointToPosition(x, y);
                if (mPosition != INVALID_POSITION) {
                    int visibleIndex = mPosition - getFirstVisiblePosition();
                    mExtendLayout = (ExtendLayout) getChildAt(visibleIndex);
                    doDelete(mExtendLayout);
                }
                restoreListItem();
                break;
            case MotionEvent.ACTION_MOVE:
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > SNAP_VELOCITY || Math.abs(x - mFirstX) > mTouchSlop &&
                        Math.abs(y - mFirstY) < mTouchSlop) {
                    mFlags |= SLIDE_MASK;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if ((mFlags & SLIDE_MASK) == SLIDE_MASK && mPosition != INVALID_POSITION
                && mExtendLayout != null) {
            float x = ev.getX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = x - mPreX;
                    if (mExtendLayout.getScrollX() - dx > 0
                            && mExtendLayout.getScrollX() - dx
                            < mExtendLayout.getDeleteButton().getWidth())
                        mExtendLayout.scrollBy((int) -dx, 0);
                    mPreX = (int) x;
                    break;
                case MotionEvent.ACTION_UP:
                    int deleteBtnWidth = mExtendLayout.getDeleteButton().getWidth();
                    if (x > mFirstX) {
                        if (Math.abs(x - mFirstX) >= deleteBtnWidth * 0.8) {
                            forwardToRight();
                        } else {
                            rollbackToLeft();
                        }
                    } else if (x < mFirstX){
                        rollbackToLeft();
                    }
                    postInvalidate();
                    mFlags &= ~SLIDE_MASK;//标记为不可滑动状态
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void rollbackToLeft() {
        int deleteBtnWidth = mExtendLayout.getDeleteButton().getWidth();
        int scrollX = mExtendLayout.getScrollX();
        int remain = deleteBtnWidth - scrollX;
        mScroller.startScroll(scrollX, 0, remain, 0, Math.abs(remain));
        mFlags &= ~DELETE_VISIBLE_MASK;
    }

    private void forwardToRight() {
        int scrollX = mExtendLayout.getScrollX();
        mScroller.startScroll(scrollX, 0, -scrollX, Math.abs(scrollX));
        mFlags |= DELETE_VISIBLE_MASK;
    }

    private void restoreListItem() {
        for (int i = 0; i < getChildCount(); i ++) {
            View child = getChildAt(i);
            if (child instanceof ExtendLayout) {
                ExtendLayout childLayout = (ExtendLayout) child;
                if (mExtendLayout != childLayout) {
                    childLayout.scrollTo(childLayout.getDeleteButton().getMeasuredWidth(), 0);
                }
            }
        }
    }

    /**
     * 删除
     * @param extendLayout extendLayout
     */
    private void doDelete(ExtendLayout extendLayout) {
        View deleteButton = extendLayout.getDeleteButton();
        if (deleteButton == null) return;
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnRemovedItemListener != null) {
                    //回调
                    mOnRemovedItemListener.itemRemoved(mPosition, getAdapter());
                }
            }
        });
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mExtendLayout.scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    /**
     * 设置列表项移除监听器
     * @param onRemovedItemListener OnRemovedItemListener
     */
    public void setOnRemovedItemListener(OnRemovedItemListener onRemovedItemListener) {
        this.mOnRemovedItemListener = onRemovedItemListener;
    }

    public interface OnRemovedItemListener {
        /**
         * 删除列表项调用
         * @param position 列表项索引
         * @param adapter 适配器
         */
        void itemRemoved(int position, ListAdapter adapter);
    }
}
