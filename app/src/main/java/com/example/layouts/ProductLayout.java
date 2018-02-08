package com.example.layouts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.activities.UserPageActivity;
import com.example.storages.ImageStorage;
import com.example.application.R;
import com.example.models.Product;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {
    private ImageView imgPicture;
    public ProductLayout(final Context ctx, Product p) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_product, this);
        String name = p.getName();
        TextView nameView  =  v.findViewById(R.id.product_text);
        nameView.setText(name);

        final String seller_id = p.getSellerId();

        TextView sellerName = v.findViewById(R.id.product_user_login);
        sellerName.setText(seller_id);
        Log.d("POD_LAYOUT_ID", seller_id == null? "none":seller_id);

        // ВОзможно костыльненько, хз
        sellerName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, UserPageActivity.class);
                intent.putExtra(UserPageActivity.USER_ID, seller_id);
                ctx.startActivity(intent);
            }
        });

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
