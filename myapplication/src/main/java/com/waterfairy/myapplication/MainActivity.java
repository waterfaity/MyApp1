package com.waterfairy.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;
    Bitmap bitmap1;
    Bitmap bitmap2;
    FlipPartView partView1;
    FlipPartView partView2;
    FlipPartView partView3;
    FlipPartView partView4;

    private FlipLayout flipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);
        initFlipLayout();
    }

    private void initFlipLayout() {
        flipLayout = (FlipLayout) findViewById(R.id.flip_layout);
        flipLayout.initDirection(FlipLayout.HORIZONTAL);
        setView2();

    }

    boolean istr = true;

    private void setView2() {
        MyAdapter myAdapter = new MyAdapter(getResources(), new int[]{R.mipmap.hor_height,
                R.mipmap.hor_width,
                R.mipmap.ver_height,
                R.mipmap.ver_width,
                R.mipmap.temp,
                R.mipmap.temp1,
                R.mipmap.timg});
        flipLayout.setAdapter(myAdapter);

    }

    private void initPartView() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ver_height);

        partView1 = (FlipPartView) findViewById(R.id.part_view1);
        partView2 = (FlipPartView) findViewById(R.id.part_view2);
        partView3 = (FlipPartView) findViewById(R.id.part_view3);
        partView4 = (FlipPartView) findViewById(R.id.part_view4);

        partView1.initData(FlipPartView.TYPE_LEFT);
        partView2.initData(FlipPartView.TYPE_RIGHT);
        partView3.initData(FlipPartView.TYPE_TOP);
        partView4.initData(FlipPartView.TYPE_BOTTOM);

        setView();
    }

    private void setView() {

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                partView1.setBitmap(bitmap);
                partView2.setBitmap(bitmap);
                partView3.setBitmap(bitmap);
                partView4.setBitmap(bitmap);


            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    public void onClick(View view) {
        setView2();
    }
}
