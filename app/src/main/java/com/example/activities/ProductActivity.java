package com.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;
import com.example.services.Services;
import com.example.storages.ImageStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ImageView imgPicture;
    private Product product;

    // В активити передается id товара
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        String id = getIntent().getStringExtra("item_id");
        // Делаем запрос по id
        Services.products.get(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                // Если все плохо, показываем тост (за здоровье сервера)
                if(!response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
                    return;
                }
                product = response.body();
                // Если товар не найден на сервере, то ВТФ?? как так-то
                // должен всегда возвращать, но на всякий случай
                if (product == null) {
                    Toast.makeText(ProductActivity.this,"No such item found", Toast.LENGTH_LONG).show();
                    return;
                }

                // Тут все очевидно на мой взгляд, и я устал уже писать все это говно
                setTitle(product.getName());
                TextView textView = (TextView) findViewById(R.id.product_activity_text);
                HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.scroll);
                final LinearLayout ll = (LinearLayout)findViewById(R.id.photos_2);
                textView.setText(product.getDescription() + "\n\n" + "Price: " + Integer.toString(product.getPrice()));
                imgPicture = (ImageView) findViewById(R.id.image);
                ImageStorage.inject(imgPicture, product.getImage(0));
                if (product.getImages().size() > 1) {
                    // если вообще есть дополнительные картинки
                    // идем по массиву и непосредственно добавляем в Layout
                    for (int i = 1; i < product.getImages().size(); i++) {
                        final String id = product.getImage(i);
                        SingleImageLayout Im = new SingleImageLayout(ProductActivity.this, id, i);
                        ll.addView(Im);
                    }
                }
                else {
                    scroll.setVisibility(View.GONE);
                }
//                imgPicture.setTag(0);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void buttonFullScreen(View v) {
        // Переход на FullScreenImageActivity
        Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
        intent.putExtra("Bitmap", product.getImage(0));
        Log.d("IMG LOG", "ASD");
        startActivity(intent);
    }
    public void showPopUp(View v) {
        // #todo
        /* Тут проблема, drawing cache всегда null, если добавить buildDrawingCache, то
        крашится. К тому же, дополнительные фото сделаны через SingleImage, а там
        onClick = "showPopUp", нужно подумать как лучше изменить. */
        int n = (int) v.getTag();

        // Переход на FullScreenImageActivity
        Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
        intent.putExtra("Bitmap", product.getImage(n));

        startActivity(intent);
    }
}
