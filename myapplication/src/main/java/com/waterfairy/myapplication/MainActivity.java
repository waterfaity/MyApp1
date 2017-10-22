package com.waterfairy.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main";
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


    private void setView2() {
        MyAdapter myAdapter = new MyAdapter(getResources(), new int[]{
                R.mipmap.temp,
                R.mipmap.temp6,
                R.mipmap.temp1,
                R.mipmap.temp5,
                R.mipmap.temp2,
                R.mipmap.temp4,
                R.mipmap.temp3
        });
        flipLayout.setAdapter(myAdapter);

    }

    private void initPartView() {

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

    boolean scale;

    public void onClick(View view) {
        int scaleRate = 1;
        if (scale) {
            scaleRate = 1;
        } else {
            scaleRate = -1;
        }
        scale = !scale;

        Log.i(TAG, "onClick: " + scaleRate);
//        flipLayout.setScaleX(scaleRate);
//
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(false);

        FlipItemView belowItemView = flipLayout.getBelowItemView();
        FlipItemView aboveItemView = flipLayout.getAboveItemView();

        FlipPartView firstView = aboveItemView.getFirstView();
        FlipPartView secondView = aboveItemView.getSecondView();

        FlipPartView firstView2 = belowItemView.getFirstView();
        FlipPartView secondView2 = belowItemView.getSecondView();

        firstView.startAnimation(rotateAnimation);
        secondView.startAnimation(rotateAnimation);
        firstView2.startAnimation(rotateAnimation);
        secondView2.startAnimation(rotateAnimation);
        aboveItemView.setAnimation(rotateAnimation);
        belowItemView.setAnimation(rotateAnimation);
//        flipLayout.startAnimation(rotateAnimation);
    }
}
