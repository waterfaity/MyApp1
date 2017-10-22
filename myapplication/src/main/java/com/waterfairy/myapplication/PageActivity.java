package com.waterfairy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        PageView pageView = (PageView) findViewById(R.id.page_view);
        MyAdapter myAdapter = new MyAdapter(getResources(), new int[]{
                R.mipmap.temp,
                R.mipmap.temp6,
                R.mipmap.temp1,
                R.mipmap.temp5,
                R.mipmap.temp2,
                R.mipmap.temp4,
                R.mipmap.temp3,
                R.mipmap.temp7,
                R.mipmap.temp8,
                R.mipmap.temp9,
                R.mipmap.temp10,
        });
        pageView.setAdapter(myAdapter);
    }
}
