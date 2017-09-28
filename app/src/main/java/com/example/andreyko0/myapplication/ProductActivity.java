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
        String text =getIntent().getStringExtra("text");
        setTitle(text);
        TextView textView = (TextView) findViewById(R.id.product_activity_text);
        textView.setText(text);
    }
}
