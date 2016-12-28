package com.david.study.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DavidChen on 2016/9/1.
 */
public class MoveHighLightTextView extends View {
    private Paint mPaint;
    private Shader mShader;
    private Matrix mMatrix;
    private int mOffset;
    Rect mRect;

    public MoveHighLightTextView(Context context) {
        this(context, null);
    }

    public MoveHighLightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveHighLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    String text;
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(150);
        mPaint.setStrokeWidth(10);
        mRect = new Rect();
        text = "智农云禽";
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        mShader = new LinearGradient(0, 0, 30, mRect.height(), Color.GRAY, Color.WHITE, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.setTranslate(mOffset, 0);
        mOffset += 3;
        mShader.setLocalMatrix(mMatrix);
        if (mOffset > mRect.width()) {
            mOffset = 0;
        }
        canvas.drawText(text, 0, 300, mPaint);
        invalidate();
    }
}
