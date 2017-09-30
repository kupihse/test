package com.example.andreyko0.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.andreyko0.myapplication.ProductActivity;
import com.example.andreyko0.myapplication.ScrollingActivity;
import com.example.application.R;
import com.example.s1k0de.entry.EntryFormActivity;

import static android.R.id.edit;

public class item_parameters extends AppCompatActivity {

    protected static String name, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_parameters);

    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        final EditText edit_name =  (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc =  (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result",result);
        setResult(ScrollingActivity.RESULT_OK, returnIntent);
        finish();
    }
}
