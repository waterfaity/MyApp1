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
    public static final int TYPE_LEFT = 1;//左
    public static final int TYPE_RIGHT = 2;//右
    public static final int TYPE_TOP = 3;//上
    public static final int TYPE_BOTTOM = 4;//下
    private static final String TAG = "flipPartView";
    private int type;//类型
    private int direction;//方向

    //默认背景颜色
    private int defaultBGColor = Color.TRANSPARENT;

    private Bitmap bitmap;//图像
    private float rotation;//角度
    private Paint bitmapPaint;//画笔

    private Rect bitmapRect;
    private Rect viewRect;

    public FlipPartView(Context context) {
        super(context);
    }

    public FlipPartView(Context context, int type) {
        super(context);
        initData(type);
    }

    public FlipPartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }


    public void initData(int type) {
        this.type = type;
        //确定方向
        if (type == TYPE_LEFT || type == TYPE_RIGHT) {
            direction = FlipLayout.HORIZONTAL;
        } else if (type == TYPE_TOP || type == TYPE_BOTTOM) {
            direction = FlipLayout.VERTICAL;
        }
        //paint
        bitmapPaint = new Paint();
        bitmapPaint.setDither(true);//防抖
        bitmapPaint.setFilterBitmap(true);//位图进行滤波处理
        //防止自动绘制
        setWillNotDraw(false);
    }

    public void setBitmap(Bitmap bitmap) {
//        if (this.bitmap != null && !bitmap.isRecycled()) {
//            this.bitmap.recycle();
//            bitmap = null;
//            System.gc();
//        }
        this.bitmap = bitmap;
        calcBitmapRect();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.i(TAG, "draw: " + type);
            calcViewRect();
            canvas.drawBitmap(bitmap, bitmapRect, viewRect, bitmapPaint);
        } else {
            //bitmap 为空  ||  bitmap被回收  ||  方向0(数据初始化失败)
            canvas.drawColor(defaultBGColor);
        }
    }

    /**
     * 计算画布边界
     */
    private void calcViewRect() {
//        if (viewRect == null) {
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
//                if (type == TYPE_RIGHT) {
//                    // 单独使用 去除该判断
//                    left = viewWidth;
//                    right = 2 * viewWidth;
//                } else {
                    right = viewWidth;
                    left = 0;
//                }
            } else if (type == TYPE_LEFT) {
                top = 0;
                bottom = viewHeight;
                right = viewWidth;
                left = (int) (viewWidth - (bitmapWidth / bitmapHeight) * viewHeight);
            } else if (type == TYPE_RIGHT) {
                top = 0;
                bottom = viewHeight;
//                    单独使用
                  left = 0;
                  right = (int) ((bitmapWidth / bitmapHeight) * viewHeight);
//                left = viewWidth;
//                right = (int) ((bitmapWidth / bitmapHeight) * viewHeight) + left;
            }
        } else if (direction == FlipLayout.VERTICAL) {
            if (!isBitmapWidthBig) {
                float tempWidth = viewHeight / (bitmapHeight / bitmapWidth);
                left = (int) ((viewWidth - tempWidth) / 2);
                right = (int) (left + tempWidth);
//                if (type == TYPE_BOTTOM) {
//                    //单独使用 去除该判断
//                    top = viewHeight;
//                    bottom = 2 * viewHeight;
//                } else {
                    bottom = viewHeight;
                    top = 0;
//                }
            } else if (type == TYPE_TOP) {
                left = 0;
                right = viewWidth;
                top = (int) (viewHeight - (bitmapHeight / bitmapWidth) * viewWidth);
                bottom = viewHeight;
            } else if (type == TYPE_BOTTOM) {
                left = 0;
                right = viewWidth;
//                单独使用
                top = 0;
                bottom = (int) ((bitmapHeight / bitmapWidth) * viewWidth);
//                top = viewHeight;
//                bottom = (int) ((bitmapHeight / bitmapWidth) * viewWidth) + top;
            }

        }
        viewRect = new Rect(left, top, right, bottom);
//        }
    }

    /**
     * 计算图片边界
     */
    private void calcBitmapRect() {
        if (bitmap != null && !bitmap.isRecycled()) {
            int bitmapWidth = 0, bitmapHeight = 0;
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
            int left = 0, right = 0, top = 0, bottom = 0;
            switch (type) {
                case TYPE_LEFT:
                    left = 0;
                    right = bitmapWidth / 2;
                    top = 0;
                    bottom = bitmapHeight;
                    break;
                case TYPE_RIGHT:
                    left = bitmapWidth - bitmapWidth / 2 + 1;
                    right = bitmapWidth;
                    top = 0;
                    bottom = bitmapHeight;
                    break;
                case TYPE_TOP:
                    left = 0;
                    right = bitmapWidth;
                    top = 0;
                    bottom = bitmapHeight / 2;
                    break;
                case TYPE_BOTTOM:
                    left = 0;
                    right = bitmapWidth;
                    top = bitmapHeight - bitmapHeight / 2 + 1;
                    bottom = bitmapHeight;
                    break;
            }
            bitmapRect = new Rect(left, top, right, bottom);
        } else {
            bitmapRect = new Rect(0, 0, 0, 0);
        }
    }

    private int getDirection() {
        return direction;
    }

    public void setDefaultBGColor(int color) {
        defaultBGColor = color;
    }
}
