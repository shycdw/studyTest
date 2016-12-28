package com.david.study.view2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.david.study.R;

import java.util.Random;

/**
 * ceshi
 * Created by DavidChen on 2016/9/2.
 */
public class GuaGuaLeView extends View {
    private static final int FINGER_WIDTH = 50;
    private static final String[] TEXT = new String[] {
            "谢谢惠顾！",
            "恭喜您中奖一万元",
            "恭喜您中奖十万元",
            "恭喜您中奖一百万元",
            "恭喜您中奖一千万元"
    };

    private Paint mPaint;
    private Paint mClearPaint;
    private Bitmap mBmpBuffer;
    private Canvas mCanvasBuffer;

    public GuaGuaLeView(Context context) {
        this(context, null);
    }

    public GuaGuaLeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaLeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(100);
        mPaint.setColor(Color.WHITE);
        mClearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClearPaint.setStrokeJoin(Paint.Join.ROUND);
        mClearPaint.setStrokeCap(Paint.Cap.ROUND);
        mClearPaint.setStrokeWidth(FINGER_WIDTH);
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //画背景
        drawBackground();
    }

    private void drawBackground() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        Bitmap bgMutable = bg.copy(Bitmap.Config.ARGB_8888, true);
        //画文字
        Canvas canvasText = new Canvas(bgMutable);
        Random random = new Random();
        int position = random.nextInt(TEXT.length);
        String text = TEXT[position];
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int x = (bgMutable.getWidth() - rect.width()) / 2;
        int y = (bgMutable.getHeight() - rect.height()) / 2;
        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setShadowLayer(10, 0, 0, Color.GREEN);
        canvasText.drawText(text, x, y, mPaint);
        mPaint.setShadowLayer(0, 0, 0, Color.YELLOW);
        //画背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(new BitmapDrawable(getResources(), bgMutable));
        } else {
            setBackgroundDrawable(new BitmapDrawable(getResources(), bgMutable));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBmpBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvasBuffer = new Canvas(mBmpBuffer);
        //画灰色
        mCanvasBuffer.drawColor(Color.parseColor("#FF808080"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBmpBuffer, 0, 0, null);
    }

    float preX,preY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = event.getX();
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCanvasBuffer.drawLine(preX, preY, event.getX(), event.getY(), mClearPaint);
                invalidate();
                preX = event.getX();
                preY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }
}
