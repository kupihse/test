package com.example.andreyko0.myapplication;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.R;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class Product extends LinearLayout {
    public Product(Context ctx, String s) {
        super(ctx);
        TextView tv  =  inflate(getContext(), R.layout.single_product, this).findViewById(R.id.product_text);
        tv.setText(s);
    }
}
