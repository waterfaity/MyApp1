package com.waterfairy.myapp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LruCache;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/22
 * des  :
 */

public class PageView extends AppCompatImageView {
    private static final String TAG = "pageView";
    private FlipAdapter adapter;
    private int viewWidth, viewHeight;
    private LruCache<Integer, Bitmap> lruCache;
    private int currentPos;


    public PageView(Context context) {
        super(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLruCache();
    }

    /**
     * 缓存处理
     */
    private void initLruCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int cacheSize = (int) (maxMemory / 8);
        lruCache = new LruCache<>(cacheSize);
    }

    private Bitmap getBitmap(int pos) {
        if (lruCache == null) return null;
        Bitmap bitmap = lruCache.get(pos);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = adapter.getBitmap(pos);
            lruCache.put(pos, bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: " + w + "--" + h);
        viewHeight = h;
        viewWidth = w;
        Log.i(TAG, "onLayout: " + viewHeight);
        invalidate();
    }

    private float startX = 0;
    private float radio;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                calcX(event.getX() - startX);
                break;
            case MotionEvent.ACTION_UP:
                if (radio > 0.5 && currentPos > 0) {
                    currentPos--;
                } else if (radio < -0.5 & currentPos < adapter.getCount() - 1) {
                    currentPos++;
                }
                radio = 0;
                invalidate();
                cacheBitmapSide();
                break;
        }

        return true;
    }

    private void cacheBitmapSide() {
        if (currentPos > 0) {
            cacheBitmap(currentPos - 1);
        }
        if (currentPos < adapter.getCount() - 1) {
            cacheBitmap(currentPos + 1);
        }
    }

    private void calcX(float dela) {
        if (viewHeight != 0 && ((dela > 0 && currentPos > 0) || (dela < 0 && currentPos < adapter.getCount() - 1))) {
            radio = dela / (viewHeight / 3);
            radio = radio > 1 ? 1 : radio < -1 ? -1 : radio;
            Log.i(TAG, "calcX: " + radio);
            invalidate();
        } else {
            radio = 0;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewHeight != 0) {
            Log.i(TAG, "onDraw: " + viewWidth);
            if (radio > 0) {
                //右滑 -> 当前右侧不动 pre左侧改变
                Bitmap currentBitmap = getBitmap(currentPos);
                Bitmap preBitmap = getBitmap(currentPos - 1);
                int viewCenterX = (int) (viewWidth * radio);
                if (Math.abs(radio) < 0.5f) {
                    int bitmapCenterX = (int) (preBitmap.getWidth() * radio);
                    //左侧:
                    canvas.drawBitmap(preBitmap,
                            getBitmapLeftRect(preBitmap, bitmapCenterX),
                            new RectF(0, 0, viewCenterX, viewHeight), null);//左侧
                    //中间:
                    canvas.drawBitmap(currentBitmap, getBitmapLeftRect(currentBitmap,
                            currentBitmap.getWidth() / 2),
                            new RectF(viewCenterX, 0, viewWidth / 2, viewHeight), null);//中
                    //右侧:
                    canvas.drawBitmap(currentBitmap,
                            getBitmapRightRect(currentBitmap, currentBitmap.getWidth() / 2),
                            new RectF(viewWidth - viewWidth / 2, 0, viewWidth, viewHeight), null);
                } else {
                    int bitmapCenterX = (int) (currentBitmap.getWidth() * radio);
                    //左侧:
                    canvas.drawBitmap(preBitmap,
                            getBitmapLeftRect(preBitmap, preBitmap.getWidth() / 2),
                            new RectF(0, 0, viewWidth / 2, viewHeight), null);//左侧
                    //中间
                    canvas.drawBitmap(preBitmap, getBitmapRightRect(preBitmap, preBitmap.getWidth() / 2),
                            new RectF(viewWidth - viewWidth / 2, 0, viewCenterX, viewHeight), null);//中
                    //右侧
                    canvas.drawBitmap(currentBitmap,
                            getBitmapRightRect(currentBitmap, bitmapCenterX),
                            new RectF(viewCenterX, 0, viewWidth, viewHeight), null);

                }
            } else if (radio < 0) {
                float radioTemp = -radio;
                //左滑 <- 左侧不动 右侧改变
                Bitmap currentBitmap = getBitmap(currentPos);
                Bitmap nextBitmap = getBitmap(currentPos + 1);
                int viewCenterX = (int) (viewWidth * (1 - radioTemp));

                if (Math.abs(radioTemp) < 0.5f) {
                    int bitmapCenterX = (int) (nextBitmap.getWidth() * (1 - radioTemp));
                    //左侧:
                    canvas.drawBitmap(currentBitmap,
                            getBitmapLeftRect(currentBitmap, currentBitmap.getWidth() / 2),
                            new RectF(0, 0, viewWidth / 2, viewHeight), null);//左侧
                    //中间:
                    canvas.drawBitmap(currentBitmap,
                            getBitmapRightRect(currentBitmap, currentBitmap.getWidth() - currentBitmap.getWidth() / 2),
                            new RectF(viewWidth / 2, 0, viewCenterX, viewHeight), null);//中
                    //右侧:
                    canvas.drawBitmap(nextBitmap,
                            getBitmapRightRect(nextBitmap, bitmapCenterX),
                            new RectF(viewCenterX, 0, viewWidth, viewHeight), null);
                } else {
                    int bitmapCenterX = (int) (currentBitmap.getWidth() * (1 - radioTemp));
                    //左侧:
                    canvas.drawBitmap(currentBitmap,
                            getBitmapLeftRect(currentBitmap, bitmapCenterX),
                            new RectF(0, 0, viewCenterX, viewHeight), null);//左侧
                    //中间:
                    canvas.drawBitmap(nextBitmap,
                            getBitmapLeftRect(nextBitmap, nextBitmap.getWidth() - nextBitmap.getWidth() / 2),
                            new RectF(viewCenterX, 0, viewWidth - viewWidth / 2, viewHeight), null);//中
                    //右侧:
                    canvas.drawBitmap(nextBitmap,
                            getBitmapRightRect(nextBitmap, nextBitmap.getWidth() - nextBitmap.getWidth() / 2),
                            new RectF(viewWidth - viewWidth / 2, 0, viewWidth, viewHeight), null);
                }

            } else if (radio == 0) {
                Bitmap bitmap = getBitmap(currentPos);
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                        new Rect(0, 0, viewWidth, viewHeight), null);//静止
            }
        }
    }

    public void setAdapter(FlipAdapter adapter) {
        this.adapter = adapter;
        if (lruCache == null) initLruCache();
        currentPos = 0;
        cacheBitmap(0);
        cacheBitmap(1);

    }

    private Bitmap cacheBitmap(int pos) {
        Bitmap cacheBitmap = lruCache.get(pos);
        if (cacheBitmap == null || cacheBitmap.isRecycled()) {
            cacheBitmap = adapter.getBitmap(pos);
            Log.i(TAG, "cacheBitmap: " + pos + "---" + cacheBitmap.getWidth());
            lruCache.put(pos, cacheBitmap);
        }
        return cacheBitmap;
    }

    private Rect getBitmapLeftRect(Bitmap bitmap, int rightX) {
        if (bitmap == null || bitmap.isRecycled()) {
            return new Rect(0, 0, 0, 0);
        } else {
            return new Rect(0, 0, rightX, bitmap.getHeight());
        }

    }

    private Rect getBitmapRightRect(Bitmap bitmap, int leftX) {
        if (bitmap == null || bitmap.isRecycled()) {
            return new Rect(0, 0, 0, 0);
        } else {
            int width = bitmap.getWidth();
            return new Rect(leftX, 0, width, bitmap.getHeight());
        }
    }
}
