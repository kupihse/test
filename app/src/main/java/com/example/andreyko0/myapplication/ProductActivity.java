package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Services.Services;
import com.example.application.R;

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
        Services.productService.getProduct(id).enqueue(new Callback<SendableProduct>() {
            @Override
            public void onResponse(Call<SendableProduct> call, Response<SendableProduct> response) {

                // Если все плохо, показываем тост (за здоровье сервера)
                if(!response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
                    return;
                }
                SendableProduct sp = response.body();
                // Если товар не найден на сервере, то ВТФ?? как так-то
                // должен всегда возвращать, но на всякий случай
                if (sp == null) {
                    Toast.makeText(ProductActivity.this,"No such item found", Toast.LENGTH_LONG).show();
                    return;
                }

                // Тут все очевидно на мой взгляд, и я устал уже писать все это говно
                Product p = sp.toProduct();
                product = p;
                setTitle(p.getName());
                TextView textView = (TextView) findViewById(R.id.product_activity_text);
                HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.scroll);
                LinearLayout ll = (LinearLayout)findViewById(R.id.photos_2);
                if (p.getImages().size() > 1) {
                    // если вообще есть дополнительные картинки
                    // идем по массиву и непосредственно добавляем в Layout
                    for (int i = 1; i < p.getImages().size(); i++) {
                        Bitmap img = p.getImage(i);
                        SingleImage Im = new SingleImage(ProductActivity.this, img, i);
                        ll.addView(Im);
                        i++;
                    }
                }
                else {
                    scroll.setVisibility(View.GONE);
                }
                textView.setText(p.getDescription() + "\n\n" + "Price: " + Integer.toString(p.getPrice()));
                imgPicture = (ImageView) findViewById(R.id.image);
                imgPicture.setImageBitmap(p.getImage(0));
//                imgPicture.setTag(0);
            }

            @Override
            public void onFailure(Call<SendableProduct> call, Throwable t) {
                Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
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
        // Переход на FullScreenImage
        Intent intent = new Intent(ProductActivity.this, FullScreenImage.class);

        // Передаем в FullScreenImage bitmap картинки и стартуем
        Bundle extras = new Bundle();
        extras.putParcelable("Bitmap", product.getImage(0));
        intent.putExtras(extras);
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
        // Переход на FullScreenImage
        Intent intent = new Intent(ProductActivity.this, FullScreenImage.class);

        // Передаем в FullScreenImage bitmap картинки и стартуем
        Bundle extras = new Bundle();
        extras.putParcelable("Bitmap", product.getImage(n));
        intent.putExtras(extras);
        startActivity(intent);
    }
}
