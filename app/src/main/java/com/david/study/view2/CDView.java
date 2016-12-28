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
 * Created by DavidChen on 2016/9/2.
 */
public class CDView extends View {
    private static final int COLOR_PURPLE = Color.parseColor("#FF904174");
    private static final int COLOR_BLUE = Color.parseColor("#FF1A88BD");
    private static final int COLOR_ORANGE = Color.parseColor("#FFD77828");
    private static final int COLOR_YELLOW = Color.parseColor("#FFEAE014");
    private static final int COLOR_RED = Color.parseColor("#FFD53326");

    private Paint mPaint;
    private Shader mShader;
    private Shader mOuterShader;
    private Matrix mMatrix;
    private float mRotate;

    public CDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShader = new SweepGradient(320, 200, new int[]{
                COLOR_PURPLE, COLOR_BLUE, COLOR_YELLOW, COLOR_RED,
                COLOR_YELLOW, COLOR_BLUE, COLOR_PURPLE, COLOR_YELLOW,
                COLOR_BLUE, COLOR_ORANGE, COLOR_BLUE, COLOR_YELLOW}, null);
        mOuterShader = new SweepGradient(340, 200, new int[]{
                Color.GRAY, Color.WHITE, Color.GRAY,
                Color.GRAY, Color.WHITE, Color.GRAY,
                Color.GRAY, Color.WHITE, Color.GRAY,
                Color.GRAY, Color.WHITE, Color.GRAY
        }, null);

        mPaint.setShader(mShader);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = mPaint;
        float x = 320;
        float y = 200;
        canvas.drawColor(Color.WHITE);
        mMatrix.setRotate(mRotate, x, y);
        mShader.setLocalMatrix(mMatrix);
        mOuterShader.setLocalMatrix(mMatrix);

        mRotate += 3;
        if (mRotate >= 360) {
            mRotate = 0;
        }
        invalidate();
        //画最外层
        paint.setShader(mOuterShader);
        canvas.drawCircle(x, y, 170, paint);
        //画界面圈
        paint.setShader(mShader);
        canvas.drawCircle(x, y, 160, paint);
        //画内圈
        paint.setShader(mOuterShader);
        canvas.drawCircle(x, y, 40, paint);
        //画黑圈
        paint.setColor(Color.BLACK);
        paint.setShader(null);
        canvas.drawCircle(x, y, 30, paint);
        //画白圈
        paint.setShader(null);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, 20, paint);
    }
}
