package com.example.activities;

/**
 * Created by 31719 on 14.01.2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.adapters.FullScreenImagePagerAdapter;
import com.example.storages.ImageStorage;
import com.example.application.R;

import java.util.ArrayList;

public class FullScreenImageActivity extends Activity {

    private FullScreenImagePagerAdapter adapter;
    private ViewPager viewPager;
    ArrayList<Bitmap> bmp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        ArrayList<String> imId = getIntent().getStringArrayListExtra("Bitmap");

        for (int i = 0; i < imId.size(); i++) {
            bmp.add(ImageStorage.get(imId.get(i)));
        }

        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImagePagerAdapter(FullScreenImageActivity.this, bmp);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}