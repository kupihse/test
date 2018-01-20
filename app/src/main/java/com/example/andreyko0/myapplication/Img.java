package com.example.andreyko0.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.example.application.R;

/**
 * Created by Andreyko0 on 19/01/2018.
 */

public class Img extends AppCompatImageView {
    public Img(Context c, Drawable d) {
        super(c);
        inflate(getContext(), R.layout.lay_img, null);
        this.setImageDrawable(d);
    }
}
