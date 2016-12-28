package com.david.study.view2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DavidChen on 2016/9/1.
 */
public class XferView extends View {

    public XferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    Bitmap dst;
    Bitmap src;
    Bitmap b3;
    Canvas c1;
    Canvas c2;
    Canvas c3;
    Paint paint;
    RectF mRectF;
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private void init() {
        dst = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        src = dst.copy(Bitmap.Config.ARGB_8888, true);
        b3 = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);

        c1 = new Canvas(dst);
        c2 = new Canvas(src);
        c3 = new Canvas(b3);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF(0, 0, 450, 450);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.GRAY);
        c1.drawCircle(180, 180, 180, paint);

        paint.setColor(Color.GREEN);
        c2.drawRect(0, 0, 300, 300, paint);

        /*int layer = c3.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);*/
        c3.drawBitmap(dst, 0, 0, null);
        paint.setXfermode(xfermode);
        c3.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);
        /*c3.restoreToCount(layer);*/
        canvas.drawBitmap(b3, 0, 0, null);
    }
}
