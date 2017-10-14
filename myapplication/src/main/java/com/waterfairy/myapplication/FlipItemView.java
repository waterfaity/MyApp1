package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class FlipItemView extends LinearLayout {
    private FlipPartView firstPart;//left  or  top
    private FlipPartView secondPart;//right or bottom

    public FlipItemView(@NonNull Context context) {
        this(context, null);
    }

    public FlipItemView(@NonNull Context context, int dir) {
        this(context, null);
        initData(dir);
    }


    public FlipItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        firstPart = new FlipPartView(context);
        secondPart = new FlipPartView(context);

    }

    public void initData(int direction) {

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

    public void setBitmap(Bitmap bitmap) {
        firstPart.setBitmap(bitmap);
        secondPart.setBitmap(bitmap);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (firstPart != null) {
            firstPart.draw(canvas);
        }
        if (secondPart != null) {
            secondPart.draw(canvas);
        }
    }
}
