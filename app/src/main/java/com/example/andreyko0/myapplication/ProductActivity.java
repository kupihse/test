package com.example.andreyko0.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.application.R;
import java.lang.Integer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ImageView imgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        String id = getIntent().getStringExtra("item_id");
        Log.d("FULL PRODUCT id", id);
        Services.productService.getProduct(id).enqueue(new Callback<SendableProduct>() {
            @Override
            public void onResponse(Call<SendableProduct> call, Response<SendableProduct> response) {

                if(!response.isSuccessful()) {
                    return;
                }
                SendableProduct sp = response.body();
                if (sp == null) {
                    return;
                }
                Product p = sp.toProduct();
                setTitle(p.getName());
                TextView textView = (TextView) findViewById(R.id.product_activity_text);
                textView.setText(p.getDescription() + "\n\n" + "Price: " + Integer.toString(p.getPrice()));
                imgPicture = (ImageView) findViewById(R.id.image);
                imgPicture.setImageBitmap(p.getImage(0));
            }

            @Override
            public void onFailure(Call<SendableProduct> call, Throwable t) {
                setTitle("failed to get");
            }
        });
//        Product p = ProductStorage.getProduct(id).toProduct();
//        if (p == null) {
//            setTitle("NULL "+id);
//        } else {

//        }
    }
}
