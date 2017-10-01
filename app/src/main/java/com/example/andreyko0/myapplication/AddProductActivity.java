package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.application.R;

public class AddProductActivity extends AppCompatActivity {

    protected static String name, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        final EditText edit_name =  (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc =  (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        final TextView params_empty =  (TextView) findViewById(R.id.empty_parameters);

        if (name.equals("") || description.equals("")) {
            params_empty.setVisibility(View.VISIBLE);
        }
        else {
            ProductStorage.addProduct(new Product(name, description));
            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
