package com.david.study.company;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 底部三角形
 * Created by DavidChen on 2016/9/9.
 */
public class TriangleUnderlineStyle implements SimpleTabStripView.UnderlineStyle {

    private static final float RADIO_TRIANGLE_WIDTH = 1/6f;

    private Paint mPaint;

    public TriangleUnderlineStyle() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }
    @Override
    public void draw(Canvas canvas, int translationX, int tabWidth, int underlineColor, int height, int underlineHeight) {
        mPaint.setColor(underlineColor);
        int mTriangleWidth = (int) (tabWidth * RADIO_TRIANGLE_WIDTH);
        int mInitTriangleWidth = tabWidth / 2 - mTriangleWidth / 2;
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(mTriangleWidth, 0);
        path.lineTo(mTriangleWidth / 2, -underlineHeight);
        path.close();
        canvas.save();
        canvas.translate(mInitTriangleWidth + translationX, height - 2);
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }
}
