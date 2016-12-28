package com.david.study.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * 复习
 * Created by DavidChen on 2016/9/1.
 */
public class WatchView extends View {
    private Paint mPaint;
    private Calendar mCalendar;
    private String[] text = new String[]{"XII", "I", "II", "III", "IV", "V", "VI", "VII", "VIII",
            "IX", "X", "XI"};

    public WatchView(Context context) {
        this(context, null);
    }

    public WatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCalendar = Calendar.getInstance();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int len = Math.min(width, height);

        //画表盘
        drawPlate(canvas, len);
        //画指针
        drawPoints(canvas, len);
        //循环画
        postInvalidateDelayed(1000);
    }

    /**
     * 画指针
     * @param canvas canvas
     * @param len 表盘的直径
     */
    private void drawPoints(Canvas canvas, int len) {
        int radius = len / 2;
        //获取当前时间
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int hours = mCalendar.get(Calendar.HOUR) % 12;
        int minutes = mCalendar.get(Calendar.MINUTE);
        int seconds = mCalendar.get(Calendar.SECOND);

        //画时针
        //角度
        int degree = 360 / 12 * hours;
        //转弧度
        double radians = Math.toRadians(degree);
        int startX = radius;
        int startY = radius;
        //此处的绘制，书上是计算终点的位置，但是要旋转90°，而个人认为，直接旋转要转的度数，画直线相对简单
        /*int endX = (int) (startX + radius * 0.5 * Math.cos(radians));
        int endY = (int) (startY + radius * 0.5 * Math.sin(radians));*/
        int endX = startX;
        int endY = (int) (startY - startY * 0.5);
        canvas.save();
        canvas.rotate(degree, radius, radius);
        //画线
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();

        //画分针
        //角度
        degree = 360 / 60 * minutes;
        //转弧度
        radians = Math.toRadians(degree);
        startX = radius;
        startY = radius;
        /*endX = (int) (startX + radius * 0.6 * Math.cos(radians));
        endY = (int) (startY + radius * 0.6 * Math.sin(radians));*/
        endX = startX;
        endY = (int) (startY - startY * 0.6);
        canvas.save();
        canvas.rotate(degree, radius, radius);
        //画线
        mPaint.setStrokeWidth(2);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();

        //画秒针
        //角度
        degree = 360 / 60 * seconds;
        //转弧度
        /*radians = Math.toRadians(degree);
        endX = (int) (startX + radius * 0.8 * Math.cos(radians));
        endY = (int) (startY + radius * 0.8 * Math.sin(radians));
        canvas.save();
        canvas.rotate(-90, radius, radius);*/
        endX = startX;
        endY = (int) (startY - startY * 0.8);
        canvas.save();
        canvas.rotate(degree, radius, radius);
        //画线
        mPaint.setStrokeWidth(1);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        //画尾巴
        /*radians = Math.toRadians(degree - 180);
        endX = (int) (startX + radius * 0.15 * Math.cos(radians));
        endY = (int) (startY + radius * 0.15 * Math.sin(radians));*/
        endX = startX;
        endY = (int) (startY + startY * 0.15);
        //画线
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();
    }

    /**
     * 画表盘
     * @param canvas canvas
     * @param len 表盘的直径
     */
    private void drawPlate(Canvas canvas, int len) {
        int radius = len / 2;
        //画圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(radius, radius, radius, mPaint);

        //画刻度
        canvas.save();
        for (int i = 0; i < 60; i++) {
            //画线
            if (i % 5 == 0) {//画长线
                /*mPaint.setColor(Color.RED);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(radius, 0, radius, 1.0f / 10 * radius, mPaint);*/
                //画罗马字符
                mPaint.setTextSize(30);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.BLACK);
                float textLength = (int) mPaint.measureText(text[i / 5]);
                float startX = radius - textLength / 2;
                float startY = 0.08f * radius;
                canvas.drawText(text[i / 5], startX, startY, mPaint);
            } else {//画短线
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(1);
                canvas.drawLine(radius, 0, radius, 1.0f / 15 * radius, mPaint);
            }
            //旋转
            canvas.rotate(6, radius, radius);
        }
        canvas.restore();
    }
}
