package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class FlipLayout extends FrameLayout {
    private Context context;
    private int minScrollCompleteLen;//最小滑动翻页的距离 定义为屏幕的1/3
    private int minScrollStartLen;//开始滑动的距离 10dp
    private static final String TAG = "flipLayout";
    private FlipItemView aboveItemView;//上层view
    private FlipItemView belowItemView;//底层view

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int direction;//方向
    private GestureDetector gestureDetector;//收拾监听
    private LruCache<Integer, Bitmap> lruCache;//缓存
    private int cacheNum = 3;//最小1
    private FlipAdapter adapter;
    private int currentPos;//当前位置
    private float scrollRate;//滑动rate  -1 0 1
    private int cacheNextPos;//显示下个图像
    private boolean ignoreMinLen;//忽略最小距离


    public FlipLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlipLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);

        initData(context);
    }

    private void initData(Context context) {
        initGesture(context);
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


    private void initView(Context context) {
        belowItemView = new FlipItemView(context, false);
        aboveItemView = new FlipItemView(context, true);
        addView(belowItemView);
        addView(aboveItemView);
        ViewGroup.LayoutParams layoutParams = aboveItemView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        belowItemView.setLayoutParams(layoutParams);
        aboveItemView.setLayoutParams(layoutParams);

    }

    private void initGesture(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return FlipLayout.this.onScroll(e1, e2);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return FlipLayout.this.onFling();
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return FlipLayout.this.onDown();
            }
        });
    }

    private boolean onUp() {
        ignoreMinLen = false;
        if (scrollRate != 0) {
            if (scrollRate == 1 && currentPos > 0) {
                currentPos--;
            } else if (scrollRate == -1 && currentPos < adapter.getCount() - 1) {
                currentPos++;
            } else {
                return false;
            }
            setCurrentPos(currentPos);
        }
        return false;
    }

    /**
     * 按下
     *
     * @return
     */
    private boolean onDown() {
        return false;
    }

    /**
     * 飞滑
     *
     * @return
     */
    private boolean onFling() {
        return false;
    }

    /**
     * 滚动
     *
     * @param e1
     * @param e2
     * @return
     */
    private boolean onScroll(MotionEvent e1, MotionEvent e2) {
        float dValue = 0;
        if (direction == VERTICAL) {
            dValue = (int) (e2.getY() - e1.getY());
        } else {
            dValue = (int) (e2.getX() - e1.getX());
        }
        if ((dValue > 0 && currentPos > 0)) {
            setCacheNextPos(currentPos - 1);
        } else if (dValue < 0 && currentPos < adapter.getCount() - 1) {
            setCacheNextPos(currentPos + 1);
        }
//        if (Math.abs(dValue) >= minScrollStartLen || ignoreMinLen) {
            //开始滑动
            ignoreMinLen = true;
            float rate = dValue / minScrollCompleteLen;
            if ((rate >= 0 && currentPos > 0) ||
                    (rate <=0 && currentPos < adapter.getCount() - 1)) {
                scrollRate = rate > 1 ? 1f : (rate < -1 ? -1f : rate);
                rotationView(scrollRate);
            } else {
                scrollRate = 0;
                Log.i(TAG, "onScroll: ");
            }
            Log.i(TAG, "onScroll: " + rate + "--" + ignoreMinLen + "--" + currentPos);
//        }
        return false;
    }

    private void rotationView(float rate) {
        scrollRate = rate;
        aboveItemView.rotation(rate);
        belowItemView.rotation(rate);
    }

    /**
     * 方向
     *
     * @param direction
     */
    public void initDirection(int direction) {
        this.direction = direction;
        belowItemView.initData(direction);
        aboveItemView.initData(direction);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (direction == VERTICAL) {
            minScrollCompleteLen = displayMetrics.heightPixels / 3;
        } else {
            minScrollCompleteLen = displayMetrics.widthPixels / 3;
        }
        minScrollStartLen = (int) (displayMetrics.density * 20);
    }

    public void setAboveBitmap(Bitmap bitmap) {
        aboveItemView.setBitmap(bitmap);
        aboveItemView.invalidate();
    }

    public void setBelowBitmap(Bitmap bitmap) {
        belowItemView.setBitmap(bitmap);
        belowItemView.invalidate();
    }

//    @Override
//    public void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        if (belowItemView != null) {
//            belowItemView.draw(canvas);
//        }
//        if (aboveItemView != null) {
//            aboveItemView.draw(canvas);
//        }
//
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onUp();
        }
        return true;
    }

    /**
     * 设置bitmap adapter
     *
     * @param adapter
     */
    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
        removeAllBindings();
        setCurrentPos(0);
        if (adapter.getCount() >= 1) {
            cacheNextPos = 0;
            setCacheNextPos(1);
        }
    }


    /**
     * 设置当前bitmap
     *
     * @param currentPos
     */
    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        Bitmap cacheBitmap = getCacheBitmap(currentPos);
        if (cacheBitmap == null || cacheBitmap.isRecycled()) {
            cacheBitmap = adapter.getBitmap(currentPos);
            cacheBitmap(currentPos, cacheBitmap);
        }
        setAboveBitmap(cacheBitmap);
        aboveItemView.reset();
        belowItemView.reset();
    }

    /**
     * 设置cache bitmap
     *
     * @param cacheNextPos
     */
    public void setCacheNextPos(int cacheNextPos) {
        if (this.cacheNextPos == cacheNextPos) {
            return;
        }
        Log.i(TAG, "setCacheNextPos: " + cacheNextPos);
        this.cacheNextPos = cacheNextPos;
        Bitmap cacheBitmap = getCacheBitmap(cacheNextPos);
        if (cacheBitmap == null || cacheBitmap.isRecycled()) {
            cacheBitmap = adapter.getBitmap(cacheNextPos);
            cacheBitmap(cacheNextPos, cacheBitmap);
        }
        setBelowBitmap(cacheBitmap);
    }

    /**
     * 缓存bitmap
     *
     * @param index
     * @param cacheBitmap
     */
    private void cacheBitmap(int index, Bitmap cacheBitmap) {
        lruCache.put(index, cacheBitmap);
    }

    /**
     * 获取bitmap
     *
     * @param index
     * @return
     */
    private Bitmap getCacheBitmap(int index) {
        return lruCache.get(index);
    }

    /**
     * 释放所有
     */
    private void removeAllBindings() {
        lruCache.evictAll();
    }

    public FlipItemView getBelowItemView() {
        return belowItemView;
    }

    public FlipItemView getAboveItemView() {
        return aboveItemView;
    }
}
