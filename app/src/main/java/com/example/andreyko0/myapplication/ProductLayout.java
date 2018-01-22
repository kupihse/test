package com.example.andreyko0.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.R;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {
    private ImageView imgPicture;
    public ProductLayout(Context ctx, Product p) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_product, this);
        imgPicture = (ImageView) findViewById(R.id.ImageView);
        imgPicture.setImageBitmap(p.getImage(0));
        String name = p.getName();
        TextView nameView  =  v.findViewById(R.id.product_text);
        nameView.setText(name);
        TextView idView = v.findViewById(R.id.product_id);
        idView.setText(p.getId());

        Integer price = p.getPrice();
        TextView priceView  =  v.findViewById(R.id.price);
        priceView.setText(Integer.toString(price) + " руб.");
        Log.d("RERENDER NAME", name);
    }
}
