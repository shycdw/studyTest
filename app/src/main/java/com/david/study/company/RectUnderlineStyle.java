package com.david.study.company;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 底部长方形样式
 * Created by DavidChen on 2016/9/9.
 */
public class RectUnderlineStyle implements SimpleTabStripView.UnderlineStyle {
    private static final float RADIO_OFFSET = 0.35f;
    Paint mPaint;
    public RectUnderlineStyle() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(Canvas canvas, int translationX, int tabWidth, int underlineColor, int height, int underlineHeight) {
        mPaint.setColor(underlineColor);
        float offset = RADIO_OFFSET * tabWidth / 2;
        canvas.save();
        canvas.translate(translationX, height - underlineHeight);
        canvas.drawRect(offset, 0, tabWidth - offset, underlineHeight, mPaint);
        canvas.restore();
    }
}
