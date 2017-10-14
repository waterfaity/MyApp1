package com.waterfairy.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/10/14
 * des  :
 */

public class MyAdapter implements FlipAdapter {
    private Resources resources;
    private int[] resIds;

    public MyAdapter(Resources resources, int[] resIds) {
        this.resources = resources;
        this.resIds = resIds;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Bitmap getBitmap(int position) {
        return BitmapFactory.decodeResource(resources,resIds[position]);
    }
}
