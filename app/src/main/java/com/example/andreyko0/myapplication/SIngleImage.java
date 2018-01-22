package com.example.andreyko0.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.application.R;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class SIngleImage extends LinearLayout {
    private ImageView img;
    String ViewId_Str;
    public SIngleImage(Context ctx, Bitmap i, int Tag) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_image, this);
        img = v.findViewById(R.id.test_image_single);
        img.setImageBitmap(i);
        img.setTag(Tag);
        
//        View v = inflate(getContext(), R.layout.single_product, this);
//        imgPicture = (ImageView) findViewById(R.id.ImageView);
//        imgPicture.setImageDrawable(p.getImage(0));
//        String name = p.getName();
//        TextView nameView  =  v.findViewById(R.id.product_text);
//        nameView.setText(name);
//        TextView idView = v.findViewById(R.id.product_id);
//        idView.setText(p.getId());
//
//        Integer price = p.getPrice();
//        TextView priceView  =  v.findViewById(R.id.price);
//        priceView.setText(Integer.toString(price) + " руб.");
    }

}
