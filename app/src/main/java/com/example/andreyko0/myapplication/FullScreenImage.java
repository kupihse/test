package com.example.andreyko0.myapplication;

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

import com.example.application.R;

public class FullScreenImage extends Activity {


    @SuppressLint("NewApi")



    // В активити передается одна картинка в виде Bitmap
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_full);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap)extras.getParcelable("Bitmap");

        ImageView imgDisplay;
        Button btnClose;


        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (Button) findViewById(R.id.btnClose);

        imgDisplay.setImageBitmap(bmp);

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });
    }
}