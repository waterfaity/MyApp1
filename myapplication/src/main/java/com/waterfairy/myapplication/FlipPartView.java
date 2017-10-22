package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class FlipPartView extends View {
    public static final int TYPE_NO = 0;//左
    public static final int TYPE_LEFT_BELOW = 1;//左
    public static final int TYPE_LEFT_ABOVE = 2;//右
    public static final int TYPE_RIGHT_BELOW = 3;//上
    public static final int TYPE_RIGHT_ABOVE = 4;//下
    private static final String TAG = "flipPartView";
    private static final int TYPE_BITMAP_1 = 1;
    private static final int TYPE_BITMAP_2 = 2;
    private int type;//类型
    private int direction;//方向

    //默认背景颜色
    private int defaultBGColor = Color.TRANSPARENT;

    private Bitmap bitmap1, bitmap2;//图像
    private float rotation;//角度
    private Paint bitmapPaint;//画笔

    private Rect bitmapRect1, bitmapRect2;
    private Rect viewRect1, viewRect2;

    public FlipPartView(Context context) {
        this(context, null);
    }

    public FlipPartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void initData(int direction, int type) {
        this.direction = direction;
        this.type = type;
        //paint
        bitmapPaint = new Paint();
        bitmapPaint.setDither(true);//防抖
        bitmapPaint.setFilterBitmap(true);//位图进行滤波处理
        //防止自动绘制
//        setWillNotDraw(false);
    }

    public void setBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        this.bitmap1 = bitmap1;
        this.bitmap2 = bitmap2;
        bitmapRect1 = calcBitmapRect(bitmap1);
        bitmapRect2 = calcBitmapRect(bitmap2);
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (bitmap1 != null && !bitmap1.isRecycled()) {
            Log.i(TAG, "draw: " + type);
            viewRect1 = calcViewRect(bitmapRect1);
            canvas.drawBitmap(bitmap1, bitmapRect1, viewRect1, bitmapPaint);
        } else {
//            bitmap1 为空  ||  bitmap被回收  ||  方向0(数据初始化失败)
            canvas.drawColor(defaultBGColor);
        }
        if (bitmap2 != null && bitmap2.isRecycled()) {
            viewRect2 = calcViewRect(bitmapRect2);
            canvas.drawBitmap(bitmap2, bitmapRect2, viewRect2, bitmapPaint);
        } else {
//            bitmap1 为空  ||  bitmap被回收  ||  方向0(数据初始化失败)
            canvas.drawColor(defaultBGColor);
        }

    }


    /**
     * 计算画布边界
     */
    private Rect calcViewRect(Rect bitmapRect) {
        int viewWidth = getRight() - getLeft();
        int viewHeight = getBottom() - getTop();
        float bitmapWidth = bitmapRect.width();
        float bitmapHeight = bitmapRect.height();

        boolean isBitmapWidthBig = (bitmapWidth / bitmapHeight) / (viewWidth / (float) viewHeight) >= 1;
        int left = 0, right = 0, top = 0, bottom = 0;

        if (direction == FlipLayout.HORIZONTAL) {
            if (isBitmapWidthBig) {
                float tempHeight = viewWidth / (bitmapWidth / bitmapHeight);
                top = (int) ((viewHeight - tempHeight) / 2);
                bottom = (int) (top + tempHeight);
                right = viewWidth;
                left = 0;
            } else if (type == TYPE_LEFT_BELOW || type == TYPE_LEFT_ABOVE) {
                top = 0;
                bottom = viewHeight;
                right = viewWidth;
                left = (int) (viewWidth - (bitmapWidth / bitmapHeight) * viewHeight);
            } else if (type == TYPE_RIGHT_ABOVE || type == TYPE_RIGHT_BELOW) {
                top = 0;
                bottom = viewHeight;
                left = 0;
                right = (int) ((bitmapWidth / bitmapHeight) * viewHeight);
            }
        } else if (direction == FlipLayout.VERTICAL) {
            if (!isBitmapWidthBig) {
                float tempWidth = viewHeight / (bitmapHeight / bitmapWidth);
                left = (int) ((viewWidth - tempWidth) / 2);
                right = (int) (left + tempWidth);
                bottom = viewHeight;
                top = 0;
            } else if (type == TYPE_LEFT_BELOW || type == TYPE_LEFT_ABOVE) {
                left = 0;
                right = viewWidth;
                top = (int) (viewHeight - (bitmapHeight / bitmapWidth) * viewWidth);
                bottom = viewHeight;
            } else if (type == TYPE_RIGHT_ABOVE || type == TYPE_RIGHT_BELOW) {
                left = 0;
                right = viewWidth;
                top = 0;
                bottom = (int) ((bitmapHeight / bitmapWidth) * viewWidth);
            }
        }
        return new Rect(left, top, right, bottom);
    }

    /**
     * 计算图片边界
     *
     * @param bitmap
     */
    private Rect calcBitmapRect(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            int bitmapWidth = 0, bitmapHeight = 0;
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
            int left = 0, right = 0, top = 0, bottom = 0;
            if (direction == FlipLayout.HORIZONTAL) {
                left = 0;
                right = bitmapWidth / 2;
                top = 0;
                bottom = bitmapHeight;
            } else {

            }
            switch (type) {
                case TYPE_LEFT_BELOW:

                    break;
                case TYPE_LEFT_ABOVE:
                    left = bitmapWidth - bitmapWidth / 2 + 1;
                    right = bitmapWidth;
                    top = 0;
                    bottom = bitmapHeight;
                    break;
                case TYPE_RIGHT_BELOW:
                    left = 0;
                    right = bitmapWidth;
                    top = 0;
                    bottom = bitmapHeight / 2;
                    break;
                case TYPE_RIGHT_ABOVE:
                    left = 0;
                    right = bitmapWidth;
                    top = bitmapHeight - bitmapHeight / 2 + 1;
                    bottom = bitmapHeight;
                    break;
            }
            return new Rect(left, top, right, bottom);

        } else {
            return new Rect(0, 0, 0, 0);
        }
    }

    private int getDirection() {
        return direction;
    }

    public void setDefaultBGColor(int color) {
        defaultBGColor = color;
    }
}
