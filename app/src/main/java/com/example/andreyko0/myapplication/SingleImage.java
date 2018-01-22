package com.example.andreyko0.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.application.R;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class SingleImage extends LinearLayout {
    private ImageView img;
    public SingleImage(Context ctx, Bitmap i, int Tag) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_image, this);
        img = v.findViewById(R.id.test_image_single);
        img.setImageBitmap(i);
        img.setTag(Tag);
    }

}
