package com.example.layouts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.storages.ImageStorage;
import com.example.application.R;
import com.example.models.Product;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {
    private ImageView imgPicture;
    public ProductLayout(Context ctx, Product p) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_product, this);
        String name = p.getName();
        TextView nameView  =  v.findViewById(R.id.product_text);
        nameView.setText(name);

        // Здесь костыль, для передачи id товара, надо сделать через теги (так вроде правильнее)
        TextView idView = v.findViewById(R.id.product_id);
        idView.setText(p.getId());

        Integer price = p.getPrice();
        TextView priceView  =  v.findViewById(R.id.price);
        priceView.setText(Integer.toString(price) + " руб.");
        imgPicture = (ImageView) findViewById(R.id.ImageView);

        final String imId = p.getImage(0);

        ImageStorage.inject(imgPicture, imId);
    }
}
