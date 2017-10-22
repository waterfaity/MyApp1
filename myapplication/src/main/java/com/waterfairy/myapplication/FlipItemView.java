package com.waterfairy.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class FlipItemView extends LinearLayout {
    private static final String TAG = "flipItemView";
    private FlipPartView firstPart;//left  or  top
    private FlipPartView secondPart;//right or bottom
    private int direction;//方向
    private boolean isAbove;//上层view

    public FlipItemView(@NonNull Context context, boolean isAbove) {
        this(context, null);
        this.isAbove = isAbove;
    }

    public FlipItemView(@NonNull Context context, int direction) {
        this(context, null);
        initData(direction);
    }


    public FlipItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        firstPart = new FlipPartView(context);
        secondPart = new FlipPartView(context);

    }

    /**
     * 初始化数据
     *
     * @param direction
     */
    public void initData(int direction) {
        this.direction = direction;
        setLayoutDirection(direction);
        addView(firstPart);
        addView(secondPart);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) firstPart.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.weight = 1;
        firstPart.setLayoutParams(layoutParams);
        secondPart.setLayoutParams(layoutParams);

        if (direction == FlipLayout.HORIZONTAL) {
            firstPart.initData(FlipPartView.TYPE_LEFT);
            secondPart.initData(FlipPartView.TYPE_RIGHT);
        } else {
            firstPart.initData(FlipPartView.TYPE_TOP);
            secondPart.initData(FlipPartView.TYPE_BOTTOM);
        }
    }

    /**
     * 设置bitmap
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        firstPart.setBitmap(bitmap);
        secondPart.setBitmap(bitmap);
        firstPart.invalidate();
        secondPart.invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        if (firstPart != null) {
//            firstPart.draw(canvas);
//        }
//        if (secondPart != null) {
//            secondPart.draw(canvas);
//        }
    }

    /**
     * 恢复正常
     */
    public void reset() {
        firstPart.setScaleX(1);
        firstPart.setScaleY(1);
        secondPart.setScaleX(1);
        secondPart.setScaleY(1);
    }

    /**
     * 旋转
     * 1. rate>0  0->1   :上 firstPart ->右(1->1 )  下 secondPart->右 (-1->1 )
     * 2. rate<0  0->-1  :上 secondPart->左(1->-1)  下 firstPart ->左 ( 1->-1)
     *
     * @param rate -1 0 1
     */
    public void rotation(float rate) {
        if (direction == FlipLayout.HORIZONTAL) {
            firstPart.setPivotX(firstPart.getRight() - firstPart.getLeft());
            secondPart.setPivotX(0);
            if (isAbove) {
                if (rate > 0) {
                    firstPart.setScaleX((-2 * rate) + 1);
                } else if (rate < 0) {
                    secondPart.setScaleX((2 * rate) + 1);
                }
            } else {
                if (rate > 0) {
                    secondPart.setScaleX((2 * rate) - 1);
                } else {
                    firstPart.setScaleX((-2 * rate) - 1);
                }
            }
        } else {
            if (isAbove) {
                if (rate > 0) {
                    firstPart.setScaleY(rate);
                } else if (rate < 0) {
                    secondPart.setScaleY(rate);
                }
            } else {
                if (rate > 0) {
                    secondPart.setScaleY(rate - 1);
                } else {
                    firstPart.setScaleY(rate);
                }
            }
        }

    }

    public FlipPartView getFirstView() {
        return firstPart;
    }

    public FlipPartView getSecondView() {
        return secondPart;
    }
}
