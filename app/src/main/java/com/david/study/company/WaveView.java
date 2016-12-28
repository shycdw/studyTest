package com.david.study.company;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 水波纹效果
 * Created by DavidChen on 2016/8/3.
 */
public class WaveView extends View {
    private static final String TAG = "WaveView";
    private static final int RED = 0xff0000;//red
    private static final int GREEN = 0x00ff00;//green
    private static final int BLUE = 0x0000ff;//blue
    private static final int[] DEFAULT_COLOR = new int[]{Color.parseColor("#F56E4B"), Color.parseColor("#F5794F"),
            Color.parseColor("#F68250"), Color.parseColor("#F88F52"), Color.parseColor("#F89A54"),
            Color.parseColor("#FDBF6E")};//默认的颜色值
    private static final int DEFAULT_BACK_POSITION = 0;//默认的背景色位置
    private static final int TIME = 30;//默认30秒刷新一次
    private static final int DEFAULT_OFFSET_DISTANCE = 5;//默认每次多偏移5的距离

    private Paint mPaint;
    private int mMinRadius;//最小半径
    private float centerX,centerY;//圆心
    private int mPerDistance;//每隔圈之间的间隔
    private int mColor[] = DEFAULT_COLOR;//个圈颜色，其中最后一个为背景色
    private int mCurrentDistance = 0;//当前每圈的
    private MyThread mThread;//循环线程
    private boolean running = false;//是否开始动画
    private int mCurrentCircles = 0;//当前的圈数
    private int mIntervalTime = TIME;//刷新时间间隔
    private static final int mOffsetDistance = DEFAULT_OFFSET_DISTANCE;//每次多偏移的距离

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int mMaxRadius = w / 2;
        int spaces = mColor.length - 1 - 1;//间隔数，例子：默认为5个圈（第一个为背景色），所以间隔为4，所以颜色数量要-1-1；
        if (spaces < 0) throw new IllegalStateException("颜色总数不能小于2");
        if (spaces == 0) {
            mPerDistance = 0;//只有一个圈时，则没有间隔
        } else {
            mPerDistance = mMaxRadius * 2 / 3 / spaces;//暂定取2/3的宽度为画圈总宽
        }
        centerX = w / 2;
        centerY = h / 2;
        mMinRadius = mMaxRadius / 3;//取1/3最大半径作为最小半径
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mThread == null) {
            mThread = new MyThread();
            mThread.start();
        } else {
            //画背景色
            setBackgroundColor(mColor[DEFAULT_BACK_POSITION]);
            //循环画圈
            for (int i = 0; i < mCurrentCircles; i++) {
                float radius = mMinRadius + (mCurrentCircles - i - 1) * mPerDistance + mCurrentDistance;
                if (i < mColor.length - 1) {
                    float scale = mCurrentDistance * 1.0f / mPerDistance;
                    int color = calculateColor(mColor[i], mColor[i + 1], scale);
                    mPaint.setColor(color);
                }
                canvas.drawCircle(centerX, centerY, radius, mPaint);
            }
        }
    }

    /**
     * 计算颜色渐变值
     * @param newColor 要变化的目标值
     * @param oldColor 最初值
     * @return 新的颜色值
     */
    private int calculateColor(int newColor, int oldColor, float scale) {
        int newRed = (newColor & RED) >> 16;
        int newGreen = (newColor & GREEN) >> 8;
        int newBlue = newColor & BLUE;
        int oldRed = (oldColor & RED) >> 16;
        int oldGreen = (oldColor & GREEN) >> 8;
        int oldBlue = oldColor & BLUE;
        newRed = (int) (oldRed + (newRed - oldRed) * scale);
        newGreen = (int) (oldGreen + (newGreen - oldGreen) * scale);
        newBlue = (int) (oldBlue + (newBlue - oldBlue) * scale);
        return Color.rgb(newRed, newGreen, newBlue);
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (running) {
                logic();
                postInvalidate();
                try {//每40ms发送一次形成扩散效果
                    Thread.sleep(mIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 逻辑处理，若当前的偏移距离超过平均的间隔，则圈数加1，若超出上限，则恢复圈数为指定值
     */
    private void logic() {
        if (mCurrentDistance + mOffsetDistance > mPerDistance) {
            mCurrentDistance = 0;
            mCurrentCircles += 1;
            if (mCurrentCircles > mColor.length - 1) {
                mCurrentCircles = mOffsetDistance;
            }
        } else {
            mCurrentDistance += mOffsetDistance;
        }
    }

    /**
     * 启动波纹
     */
    public void startWave() {
        running = true;
        if (mThread == null) {
            mThread = new MyThread();
            mThread.start();
        } else {
            mThread.start();
        }
    }

    /**
     * 设置刷新时间，默认30ms
     * @param intervalTime 刷新间隔时间
     */
    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }
}
