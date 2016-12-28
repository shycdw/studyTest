package com.david.study.view2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.david.study.R;

/**
 * test
 * Created by DavidChen on 2016/9/2.
 */
public class MaskView extends View {
    private Paint mPaint;
    private Bitmap mDst;
    private Bitmap mMask;

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDst = BitmapFactory.decodeResource(getResources(), R.mipmap.test2);
        mMask = BitmapFactory.decodeResource(getResources(), R.mipmap.github);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layer = canvas.saveLayer(0, 0, mDst.getWidth(), mDst.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mDst, 0, 0, null);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mMask, 0, 0, mPaint);
        canvas.restoreToCount(layer);
        mPaint.setXfermode(null);
    }
}
