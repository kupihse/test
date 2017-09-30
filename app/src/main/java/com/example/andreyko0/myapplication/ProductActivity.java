package com.example.andreyko0.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.application.R;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        String id = getIntent().getStringExtra("item_id");
        Product p = ProductStorage.getProduct(id);
        if (p == null) {
            setTitle("NULL "+id);
        } else {
            setTitle(p.getName());
            TextView textView = (TextView) findViewById(R.id.product_activity_text);
            textView.setText(p.getDesctiption());
        }
    }
}
