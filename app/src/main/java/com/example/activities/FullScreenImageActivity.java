package com.example.activities;

/**
 * Created by 31719 on 14.01.2018.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.storages.ImageStorage;
import com.example.application.R;

public class FullScreenImageActivity extends Activity {


    @SuppressLint("NewApi")



    // В активити передается одна картинка в виде Bitmap
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_full);

        Bundle extras = getIntent().getExtras();
        String imId = getIntent().getStringExtra("Bitmap");
        Bitmap bmp = ImageStorage.get(imId);

        ImageView imgDisplay;
        Button btnClose;


        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (Button) findViewById(R.id.btnClose);

        imgDisplay.setImageBitmap(bmp);

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImageActivity.this.finish();
            }
        });
    }
}