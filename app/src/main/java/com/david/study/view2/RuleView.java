package com.david.study.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 测试
 * Created by DavidChen on 2016/9/1.
 */
public class RuleView extends View {
    private Paint mPaint;

    public RuleView(Context context) {
        this(context, null);
    }

    public RuleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Rect mRect;
    RectF mRectF;
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mRect = new Rect();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();

        int baseLineHeight = 50;
        float millWidth = width * 1.0f / 140;
        //画外框
        mPaint.setStyle(Paint.Style.STROKE);
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = width;
        mRectF.bottom = 3 * baseLineHeight;
        canvas.drawRoundRect(mRectF, 10, 10, mPaint);
        //画刻度以及文字
        canvas.save();
        canvas.translate(5 * millWidth, 0);
        for (int i = 0; i <= 130; i++) {
            if (i % 10 == 0){//画长线
                canvas.drawLine(0, 3 * baseLineHeight, 0, baseLineHeight, mPaint);
                //画文字
                String text = String.valueOf(i / 10);
                mPaint.getTextBounds(text, 0, text.length(), mRect);
                mPaint.setTextSize(20);
                canvas.drawText(text, - mRect.width() / 2, baseLineHeight - mRect.height() + 5, mPaint);
                if (i / 10 == 0) {
                    canvas.drawText("cm", mRect.width() / 2 + 5, baseLineHeight - mRect.height() + 5, mPaint);
                }
            } else if (i % 5 == 0){//画中线
                canvas.drawLine(0, 3 * baseLineHeight, 0, 1.5f * baseLineHeight, mPaint);
            } else {//画短线
                canvas.drawLine(0, 3 * baseLineHeight, 0, 2 * baseLineHeight, mPaint);
            }
            canvas.translate(millWidth, 0);
        }
        canvas.restore();
    }
}
