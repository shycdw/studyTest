package com.david.study.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DavidChen on 2016/9/1.
 */
public class Sweep extends View {
    private Paint mPaint;
    private float mRotate;
    private Matrix mMatrix;
    private Shader mShader;
    float x = 160;
    float y = 100;

    public Sweep(Context context) {
        this(context, null);
    }

    public Sweep(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMatrix = new Matrix();
        mShader = new SweepGradient(x, y, new int[]{Color.GREEN, Color.RED, Color.BLUE, Color.GREEN}, null);
        mPaint.setShader(mShader);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(300, 300);
        canvas.drawColor(Color.WHITE);
        mMatrix.setRotate(mRotate, x, y);
        mShader.setLocalMatrix(mMatrix);
        mRotate += 3;
        if (mRotate >= 360) {
            mRotate = 0;
        }
        canvas.drawCircle(x, y, 380, mPaint);
        invalidate();
    }
}
