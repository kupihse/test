package com.example.andreyko0.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ImageView imgPicture;


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
                setTitle(p.getName());
                TextView textView = (TextView) findViewById(R.id.product_activity_text);
                textView.setText(p.getDescription() + "\n\n" + "Price: " + Integer.toString(p.getPrice()));
                imgPicture = (ImageView) findViewById(R.id.image);
                imgPicture.setImageBitmap(p.getImage(0));
            }

            @Override
            public void onFailure(Call<SendableProduct> call, Throwable t) {
                Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
            }
        });
    }
}
