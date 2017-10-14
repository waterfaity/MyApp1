package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> cacheNumList = new ArrayList<>();
    private LruCache<Integer, Bitmap> lruCache;//缓存
    private int cacheNum = 3;//最小1
    private FlipAdapter adapter;
    private int currentPos;//当前位置
    private float scrollRate;//滑动rate  -1 0 1


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
        lruCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {   //API 19
                    return bitmap.getAllocationByteCount();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) { //API 12
                    return bitmap.getByteCount();
                }
                return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
            }
        };
    }

    private void cacheBitmap(int index, Bitmap cacheBitmap) {
        for (int i = 0; cacheNumList.size() >= cacheNum && i < cacheNumList.size(); i++) {
            Integer cacheIndex = cacheNumList.get(i);
            if (Math.abs(cacheIndex - index) > (cacheNum / 2)) {
                //取出数据bitmap
                Bitmap bitmap = lruCache.get(cacheIndex);
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }
                    lruCache.remove(i);
                    cacheNumList.remove(i);
                    i = 0;
                    Log.i(TAG, "cacheBitmap: 清除数据" + cacheIndex + " - " + index);
                }

            }
        }
        cacheNumList.add(index);
        lruCache.put(index, cacheBitmap);
        Log.i(TAG, "cacheBitmap: 保存");
    }

    private Bitmap getCacheBitmap(int index) {
        return lruCache.get(index);
    }

    private void initView(Context context) {
        belowItemView = new FlipItemView(context);
        aboveItemView = new FlipItemView(context);
        addView(belowItemView);
        addView(aboveItemView);
        ViewGroup.LayoutParams layoutParams = belowItemView.getLayoutParams();
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
        Log.i(TAG, "onUp: ");
        if (scrollRate == 1) {
            currentPos--;
        } else if (scrollRate == -1) {
            currentPos++;
        }
        setCurrentPos(currentPos);
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
        if (Math.abs(dValue) >= minScrollStartLen) {
            //开始滑动
            float rate = (dValue - minScrollStartLen) /
                    (minScrollCompleteLen - minScrollStartLen);
            if ((rate > 0 && currentPos > 0) ||
                    (rate < 0 && currentPos < adapter.getCount() - 1)) {
                scrollRate = rate > 1 ? 1f : (rate < -1 ? -1f : rate);
                rotationView(scrollRate);
            } else {
                scrollRate = 0;
            }
        }
        return false;
    }

    private void rotationView(float rate) {
        scrollRate = rate;
    }

    /**
     * 方向
     *
     * @param direction
     */
    public void initDirection(int direction) {
        this.direction = direction;
        aboveItemView.initData(direction);
        belowItemView.initData(direction);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (direction == VERTICAL) {
            minScrollCompleteLen = displayMetrics.heightPixels / 3;
        } else {
            minScrollCompleteLen = displayMetrics.widthPixels / 3;
        }
        minScrollStartLen = (int) (displayMetrics.density * 10);
    }

    public void setAboveBitmap(Bitmap bitmap) {
        aboveItemView.setBitmap(bitmap);
        invalidate();
    }

    public void setBelowBitmap(Bitmap bitmap) {
        belowItemView.setBitmap(bitmap);
        invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        Log.i(TAG, "dispatchDraw: ");
        if (aboveItemView != null) {
            aboveItemView.draw(canvas);
        }
        if (belowItemView != null) {
            belowItemView.draw(canvas);
        }
    }

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
        setCurrentPos(0);
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        Bitmap cacheBitmap = lruCache.get(currentPos);
        if (cacheBitmap == null || cacheBitmap.isRecycled()) {
            Log.i(TAG, "new bitmap " + currentPos);
            cacheBitmap = adapter.getBitmap(currentPos);
            cacheBitmap(currentPos, cacheBitmap);
        } else {
            Log.i(TAG, "get bitmap " + currentPos);
        }
        setAboveBitmap(cacheBitmap);
    }
}
