package com.david.study.view3;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by DavidChen on 2016/10/26.
 */
public class StickyLayout extends LinearLayout {

    private static final String TAG = "StickyLayout";
    private static final int STATUS_EXPANDED = 1;
    private static final int STATUS_COLLAPSED = 2;

    public interface OnGiveUpTouchListener {
        /**
         * 判断是否停止滑动
         *
         * @return true，已经到顶，false，未到顶
         */
        boolean giveUpTouchEvent();
    }

    private int mLastX;
    private int mLastY;
    private int mStatus = STATUS_EXPANDED;
    private View mHeader;
    private int mHeaderHeight;
    private int mCurHeaderHeight;

    private int mTouchSlop;
    private OnGiveUpTouchListener mListener;

    public StickyLayout(Context context) {
        this(context, null);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setOnGiveUpTouchListener(OnGiveUpTouchListener l) {
        this.mListener = l;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (mHeader == null)) {
            initData();
        }
    }

    private void initData() {
        int count = getChildCount();
        if (count < 2) {
            throw new IllegalStateException("Child count must more than 2!");
        }
        mHeader = getChildAt(0);
        mHeaderHeight = mHeader.getMeasuredHeight();
        mCurHeaderHeight = mHeaderHeight;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Log.i("NET", "intercept:" + x +"," + y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE://相同方向上的冲突，要根据业务逻辑来判断是否拦截
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) >= Math.abs(deltaY)) { // y方向移动距离小于x方向，则不拦截
                    intercept = false;
                } else if (mStatus == STATUS_EXPANDED && deltaY < -mTouchSlop) { // 已经展开了，且向上滑动距离超过最小滑动距离，则拦截
                    intercept = true;
                } else if (mListener != null) {
                    if (mListener.giveUpTouchEvent() && deltaY > mTouchSlop) {//如果包含的listView已经到顶，则拦截
                        intercept = true;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                mLastX = mLastY = 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {//此处不用处理down事件，因为不会拦截
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                mCurHeaderHeight += deltaY;
                Log.i("NET", "MOVE:" + mCurHeaderHeight + "  " + deltaY);
                setCurHeaderHeight(mCurHeaderHeight);
                break;
            case MotionEvent.ACTION_UP:
                int dest;
                Log.i("NET", "UP:" + mCurHeaderHeight);
                if (mCurHeaderHeight <= 0.5f * mHeaderHeight) { // 平滑滑动到顶部
                    dest = 0;
                    mStatus = STATUS_COLLAPSED;
                } else { // 平滑滑动到底部
                    dest = mHeaderHeight;
                    mStatus = STATUS_EXPANDED;
                }
                // 平滑滑动到目标位置
                this.smoothScrollTo(mCurHeaderHeight, dest, 500);
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    /**
     * 平滑滚动到目标位置（此处其实不是滚动，而是逐渐改变header的高度，形成一种滑动的效果）
     * @param curHeaderHeight 目前高度
     * @param destHeaderHeight 目标高度
     * @param duration 时间，ms
     */
    private void smoothScrollTo(int curHeaderHeight, int destHeaderHeight, long duration) {
        smoothScrollTo(curHeaderHeight, destHeaderHeight, duration, false);
    }

    private void smoothScrollTo(final int curHeaderHeight, final int destHeaderHeight, long duration, final boolean modifyHeaderHeight) {
        final int frameCount  = (int) (duration / 1000f * 30) + 1;
        final float partition = (destHeaderHeight - curHeaderHeight) / (float) frameCount;
        new Thread("Thread#smoothSetHeaderHeight") {
            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = destHeaderHeight;
                    } else {
                        height = (int) (curHeaderHeight + i * partition);
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {
                            setCurHeaderHeight(height);
                        }
                    });

                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (modifyHeaderHeight) {
                        setHeaderHeight(destHeaderHeight);
                    }
                }
            }
        }.run();
    }

    private void setHeaderHeight(int destHeaderHeight) {

        mHeaderHeight = destHeaderHeight;
    }

    private void setHeaderHeight(int headerHeight, boolean modifyHeaderHeight) {
        if (modifyHeaderHeight) {
            setHeaderHeight(headerHeight);
        }
        setHeaderHeight(headerHeight);
    }

    public int getHeightHeight() {
        return mCurHeaderHeight;
    }

    private void setCurHeaderHeight(int headerHeight) {
        if (headerHeight < 0) { //判断此时的头部大小，不能超过最大值也不能低于0
            headerHeight = 0;
        } else if (headerHeight > mHeaderHeight) {
            Log.i("NET", mHeaderHeight + "setCurHeaderHeight:" + mCurHeaderHeight);
            headerHeight = mHeaderHeight;
        }

        if (headerHeight == 0) {//重置此时的头部状态
            mStatus = STATUS_COLLAPSED;
        } else if (headerHeight == mHeaderHeight) {
            mStatus = STATUS_EXPANDED;
        }

        if (mHeader != null && mHeader.getLayoutParams() != null) { //主要判断header是否为空，不为空则可以设置
            mHeader.getLayoutParams().height = headerHeight;
            mHeader.requestLayout();
            mCurHeaderHeight = headerHeight;
        }
    }
}
