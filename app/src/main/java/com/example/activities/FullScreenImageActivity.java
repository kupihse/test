package com.example.activities;

/**
 * Created by 31719 on 14.01.2018.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.activities.FullScreenImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.storages.ImageStorage;
import com.example.application.R;

import java.util.ArrayList;

public class FullScreenImageActivity extends Activity {


//    @SuppressLint("NewApi")
//
//
//
//    // В активити передается одна картинка в виде Bitmap
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.layout_full);
//
//        Bundle extras = getIntent().getExtras();
//        String imId = getIntent().getStringExtra("Bitmap");
//        Bitmap bmp = ImageStorage.get(imId);
//
//        ImageView imgDisplay;
//        Button btnClose;
//
//
//        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
//        btnClose = (Button) findViewById(R.id.btnClose);
//
//        imgDisplay.setImageBitmap(bmp);
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FullScreenImageActivity.this.finish();
//            }
//        });
//    }


    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    ArrayList <Bitmap> bmp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> imId = getIntent().getStringArrayListExtra("Bitmap");

        for (int i = 0; i < imId.size(); i++) {
            bmp.add(ImageStorage.get(imId.get(i)));
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImageAdapter(FullScreenImageActivity.this, bmp);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}