package com.waterfairy.myapp1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by water_fiay on 2017/10/11.
 * 995637517@qq.com
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int[] resIds;

    public MyAdapter(Context context, int[] resIds) {
        this.resIds = resIds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(context);
        }
        ((ImageView) convertView).setImageResource(resIds[position]);
        return convertView;
    }
}
