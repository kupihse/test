package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.application.R;

public class AddProductActivity extends AppCompatActivity {

    protected static String name, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        final EditText edit_name =  (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc =  (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        ProductStorage.addProduct(new Product(name, description));
        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result",result);
        setResult(ScrollingActivity.RESULT_OK, returnIntent);
        finish();
    }
}
