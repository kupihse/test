package com.example.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.SendableImage;
import com.example.storages.DiskLruCache;
import com.example.storages.ImageStorage;
import com.example.services.Services;
import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ImageView imgPicture;
    private Product product;

    //test
    private Bitmap bitmap;
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
                ProductActivity.this.bitmap = ImageStorage.get(product.getImage(0));
                imgPicture.setImageBitmap(ProductActivity.this.bitmap);
                if (product.getImages().size() > 1) {
                    // если вообще есть дополнительные картинки
                    // идем по массиву и непосредственно добавляем в Layout
                    for (int i = 1; i < product.getImages().size(); i++) {
                        final String id = product.getImage(i);
                        Bitmap img;
                        final int i2 = i;
                        if (ImageStorage.has(id)) {
                            SingleImageLayout Im = new SingleImageLayout(ProductActivity.this, ImageStorage.get(id), i2);
                            ll.addView(Im);
                        } else {
                            Services.images.get(id).enqueue(new Callback<SendableImage>() {
                                @Override
                                public void onResponse(Call<SendableImage> call, Response<SendableImage> response) {
                                    if (!response.isSuccessful()) {
                                        // maybe #todo
                                        return;
                                    }
                                    SendableImage encImg = response.body();
                                    if (encImg == null) {
                                        // maybe #todo
                                        return;
                                    }
                                    byte[] decodedString = Base64.decode(encImg.body, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    ImageStorage.set(id, decodedByte);
                                    SingleImageLayout Im = new SingleImageLayout(ProductActivity.this, decodedByte, i2);
                                    ll.addView(Im);
                                }

                                @Override
                                public void onFailure(Call<SendableImage> call, Throwable t) {

                                }
                            });
                        }
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
        DiskLruCache.init(getExternalCacheDir());
        Button button = (Button) findViewById(R.id.product_activity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     DiskLruCache.setFile(product.getImage(0), bitmap);
            }
        });
    }

    public void buttonFullScreen(View v) {
        // #todo
        /* Тут проблема, drawing cache всегда null, если добавить buildDrawingCache, то
        крашится. К тому же, дополнительные фото сделаны через SingleImage, а там
        onClick = "showPopUp", нужно подумать как лучше изменить. */
//        int n = (int) v.getTag();

//        ImageView view = (ImageView)findViewById(v.getId());
//        view.buildDrawingCache(true);
//        Bitmap image = view.getDrawingCache();

//        if(view.getDrawingCache() == null) {
//            Log.d("Drawing cache", "cache is null");
//        }
        // Переход на FullScreenImageActivity
        Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
//        Bundle extras = new Bundle();
//        extras.putParcelable("Bitmap", product.getImage(0));
//        intent.putExtras(extras);
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

//        ImageView view = (ImageView)findViewById(v.getId());

//        view.buildDrawingCache(true);
//        Bitmap image = view.getDrawingCache();

//        if(view.getDrawingCache() == null) {
//            Log.d("Drawing cache", "cache is null");
//        }
        // Переход на FullScreenImageActivity
        Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
        intent.putExtra("Bitmap", product.getImage(n));

        startActivity(intent);
    }
}
