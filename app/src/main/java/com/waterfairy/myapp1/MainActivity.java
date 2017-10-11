package com.waterfairy.myapp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.foldablelayout.FoldableListLayout;


public class MainActivity extends AppCompatActivity {
    private FoldableListLayout foldableListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foldableListLayout = (FoldableListLayout) findViewById(R.id.foldable_layout);
        foldableListLayout.setAdapter(new MyAdapter(this, new int[]{
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher}));
    }
}
