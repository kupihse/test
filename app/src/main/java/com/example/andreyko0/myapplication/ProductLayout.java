package com.example.andreyko0.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Services.ImageCache;
import com.example.Services.Services;
import com.example.application.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (ImageCache.has(imId)) {
            imgPicture.setImageBitmap(ImageCache.get(imId));
            return;
        }

        Services.images.get(p.getImage(0)).enqueue(new Callback<Services.SendableImage>() {
            @Override
            public void onResponse(Call<Services.SendableImage> call, Response<Services.SendableImage> response) {
                if (!response.isSuccessful()) {
                    // maybe #todo
                    return;
                }
                Services.SendableImage encImg = response.body();
                if (encImg == null) {
                    // maybe #todo
                    return;
                }
                byte[] decodedString = Base64.decode(encImg.body, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                imgs.add(decodedByte);
                ImageCache.set(imId, decodedByte);
                imgPicture.setImageBitmap(decodedByte);
            }

            @Override
            public void onFailure(Call<Services.SendableImage> call, Throwable t) {
            }
        });
    }
}
