package com.example.layouts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.activities.ProductActivity;
import com.example.activities.UserPageActivity;
import com.example.storages.ImageStorage;
import com.example.application.R;
import com.example.models.Product;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {
    public ProductLayout(final Context ctx, Product p) {
        super(ctx);

        final String productId = p.getId();

        inflate(getContext(), R.layout.single_product, this);

        String name = p.getName();

        TextView nameView  =  findViewById(R.id.product_text);
        nameView.setText(name);
        nameView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doClick(ctx, productId);
            }
        });



        final String seller_id = p.getSellerId();

        TextView sellerName = findViewById(R.id.product_user_login);
        sellerName.setText(seller_id);
        Log.d("POD_LAYOUT_ID", seller_id == null? "none":seller_id);

        sellerName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, UserPageActivity.class);
                intent.putExtra(UserPageActivity.USER_ID, seller_id);
                ctx.startActivity(intent);
            }
        });



        Integer price = p.getPrice();
        TextView priceView  =  findViewById(R.id.price);
        priceView.setText(Integer.toString(price) + " руб.");

        TextView date = findViewById(R.id.product_date);
        date.setText("Today");



        ImageView imgPicture = findViewById(R.id.ImageView);
        imgPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doClick(ctx, productId);
            }
        });

        final String imId = p.getImage(0);
        ImageStorage.inject(imgPicture, imId);
    }

    private void doClick(Context ctx, String id) {
        Intent productIntent = new Intent(ctx, ProductActivity.class);
        productIntent.putExtra("item_id", id);
        ctx.startActivity(productIntent);
    }
}
