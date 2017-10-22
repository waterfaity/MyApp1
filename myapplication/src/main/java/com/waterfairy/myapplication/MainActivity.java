package com.waterfairy.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main";
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
}
