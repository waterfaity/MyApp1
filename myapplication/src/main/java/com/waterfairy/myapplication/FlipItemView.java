package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class FlipItemView extends LinearLayout {
    private static final String TAG = "flipItemView";
    private FlipPartView belowPart;//below
    private FlipPartView abovePart;//above
    private int direction;//方向
    private boolean isLeft;//上层view

    public FlipItemView(@NonNull Context context, boolean isLeft) {
        this(context, null);
        this.isLeft = isLeft;
    }

    public FlipItemView(@NonNull Context context, int direction) {
        this(context, null);
        initData(direction);
    }


    public FlipItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        belowPart = new FlipPartView(context);
        abovePart = new FlipPartView(context);

    }

    /**
     * 初始化数据
     *
     * @param direction
     */
    public void initData(int direction) {
        this.direction = direction;
        setLayoutDirection(direction);
        addView(belowPart);
        addView(abovePart);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) belowPart.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.weight = 1;
        belowPart.setLayoutParams(layoutParams);
        abovePart.setLayoutParams(layoutParams);

        belowPart.initData(direction, isLeft ? FlipPartView.TYPE_LEFT_BELOW : FlipPartView.TYPE_RIGHT_BELOW);
        abovePart.initData(direction, isLeft ? FlipPartView.TYPE_LEFT_ABOVE : FlipPartView.TYPE_RIGHT_ABOVE);
    }

    /**
     * 设置bitmap
     *
     * @param bitmap1
     * @param bitmap2
     */
    public void setBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        belowPart.setBitmap(bitmap1, bitmap2);
        abovePart.setBitmap(bitmap1, bitmap2);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    /**
     * 恢复正常
     */
    public void reset() {
        belowPart.setScaleX(1);
        belowPart.setScaleY(1);
        abovePart.setScaleX(1);
        abovePart.setScaleY(1);
    }

    /**
     * 旋转
     * 1. rate>0  0->1   :上 belowPart ->右(1->1 )  下 abovePart->右 (-1->1 )
     * 2. rate<0  0->-1  :上 abovePart->左(1->-1)  下 belowPart ->左 ( 1->-1)
     *
     * @param rate -1 0 1
     */
    public void rotation(float rate) {
        if (direction == FlipLayout.HORIZONTAL) {
            belowPart.setPivotX(belowPart.getRight() - belowPart.getLeft());
            abovePart.setPivotX(0);
            if (isLeft) {
                if (rate > 0) {
                    belowPart.setScaleX((-2 * rate) + 1);
                } else if (rate < 0) {
                    abovePart.setScaleX((2 * rate) + 1);
                }
            } else {
                if (rate > 0) {
                    abovePart.setScaleX((2 * rate) - 1);
                } else {
                    belowPart.setScaleX((-2 * rate) - 1);
                }
            }
        } else {
            if (isLeft) {
                if (rate > 0) {
                    belowPart.setScaleY(rate);
                } else if (rate < 0) {
                    abovePart.setScaleY(rate);
                }
            } else {
                if (rate > 0) {
                    abovePart.setScaleY(rate - 1);
                } else {
                    belowPart.setScaleY(rate);
                }
            }
        }

    }

    public FlipPartView getFirstView() {
        return belowPart;
    }

    public FlipPartView getSecondView() {
        return abovePart;
    }
}
