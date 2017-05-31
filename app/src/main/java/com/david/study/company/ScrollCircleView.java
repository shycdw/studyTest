package com.david.study.company;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.david.study.R;

/**
 * 外圈圆滚动成圆，内部一个勾
 * Created by DavidChen on 2017/5/26.
 */

public class ScrollCircleView extends View {

    private static final int DEFAULT_STROKE_COLOR = Color.parseColor("#FF37B375"); // 绿色
    private static final float STROKE_WIDTH_RATIO = 2 / 19f;
    private static final int DEFAULT_SIZE = 280;
    private static final float LEFT_TOP_X_RATIO = 1 / 2f; // 相对于圆心，下同
    private static final float LEFT_TOP_Y_RATIO = 1 / 12f;
    private static final float BOTTOM_X_RATIO = 1 / 7f;
    private static final float BOTTOM_Y_RATIO = 1 / 3f;
    private static final float RIGHT_TOP_X_RATIO = 1 / 2f;
    private static final float RIGHT_TOP_Y_RATIO = 1 / 3f;

    private float mDestAngle;
    private Paint mPaint;
    private RectF mRectF;
    private Path mPath;
    private float mStrokeWidth;

    public ScrollCircleView(Context context) {
        this(context, null);
    }

    public ScrollCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollCircleView);
        int strokeColor = ta.getColor(R.styleable.ScrollCircleView_scv_stroke_color, DEFAULT_STROKE_COLOR);
        mStrokeWidth = ta.getFloat(R.styleable.ScrollCircleView_scv_stroke_size, -1);
        ta.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(strokeColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        mDestAngle += 7.2f;
        if (mDestAngle < 360) {
            canvas.drawArc(mRectF, 0, -mDestAngle, false, mPaint);
            postInvalidateDelayed(10);
        } else {
            canvas.drawArc(mRectF, 0, -360, false, mPaint); // 最后静止成一个圆
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float halfSize = Math.min(w, h) / 2f;
        float halfW = w / 2;
        float halfH = h / 2;
        float strokeWidth = halfSize * STROKE_WIDTH_RATIO;
        float radius = halfSize - strokeWidth / 2;
        if (mStrokeWidth <= 0) {
            mStrokeWidth = radius * STROKE_WIDTH_RATIO;
        }
        mPaint.setStrokeWidth(mStrokeWidth);
        mRectF = new RectF(halfW - radius, halfH - radius, halfW + radius, halfH + radius);
        mPath.reset();
        mPath.moveTo(halfW - radius * LEFT_TOP_X_RATIO, halfH - radius * LEFT_TOP_Y_RATIO);
        mPath.lineTo(halfW - radius * BOTTOM_X_RATIO, halfH + radius * BOTTOM_Y_RATIO);
        mPath.lineTo(halfW + radius * RIGHT_TOP_X_RATIO, halfH - radius * RIGHT_TOP_Y_RATIO);
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            height = DEFAULT_SIZE;
        }
        return height;
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            width = DEFAULT_SIZE;
        }
        return width;
    }
}
