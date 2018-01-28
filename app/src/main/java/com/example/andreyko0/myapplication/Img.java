package com.example.andreyko0.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;

import com.example.application.R;

/**
 * Created by Andreyko0 on 19/01/2018.
 */

// Пока что не испоьзуется, мб потом заменит SingleImage

public class Img extends AppCompatImageView {
    public Img(Context c, Drawable d) {
        super(c);
        inflate(getContext(), R.layout.lay_img, null);
        this.setImageDrawable(d);
    }
}
