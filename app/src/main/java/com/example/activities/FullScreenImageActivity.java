package com.example.activities;

/**
 * Created by 31719 on 14.01.2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.adapters.FullScreenImagePagerAdapter;
import com.example.layouts.SingleImageLayout;
import com.example.storages.ImageStorage;
import com.example.application.R;

import java.util.ArrayList;

public class FullScreenImageActivity extends Activity {

    private FullScreenImagePagerAdapter adapter;
    private ViewPager viewPager;
    ArrayList<Bitmap> bmp = new ArrayList<>();
    private LinearLayout allPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);
        final ArrayList<String> imId = getIntent().getStringArrayListExtra("Bitmap");

        for (int i = 0; i < imId.size(); i++) {
            bmp.add(ImageStorage.get(imId.get(i)));
        }

        allPhotos = findViewById(R.id.all_photos);
        int j = 555;

        if (imId.size() < 2) {
            allPhotos.setVisibility(View.INVISIBLE);
        }
        else {
            allPhotos.removeAllViews();
            // идем по всему их массиву и непосредственно добавляем в Layout
            for (String imgId : imId) {
                final SingleImageLayout Im = new SingleImageLayout(this, imgId, j);
                allPhotos.addView(Im);
                allPhotos.findViewWithTag(j).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int n = (int) v.getTag() - 555;
                        viewPager.setCurrentItem(n);
//                        for (int idx = 0; idx < imId.size(); idx++) {
//                            if (idx != n) {
//                                allPhotos.findViewWithTag(idx + 555)
//                                        .setPadding(0,0,0,0);
//                            }
//                        }
//                        v.setPadding(5,0,5,20);
                    }
                });
                j++;
            }
        }

        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int idx = 0; idx < imId.size(); idx++) {
                    if (idx != position) {
                        allPhotos.findViewWithTag(idx + 555)
                                .setPadding(0,0,0,0);
                    }
                }
                allPhotos.findViewWithTag(position + 555).setPadding(5,0,5,20);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        allPhotos.findViewWithTag(position + 555).setPadding(5,0,5,20);

        adapter = new FullScreenImagePagerAdapter(FullScreenImageActivity.this, bmp);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageActivity.this.finish();
            }
        });

    }

}