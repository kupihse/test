package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.application.R;

import org.w3c.dom.Text;

public class ScrollingActivity extends AppCompatActivity {

    private LinearLayout ll;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll = (LinearLayout) findViewById(R.id.products);
    }
    public void doClick(View v) {
          TextView tv = v.findViewById(R.id.product_text);
        Intent productIntent = new Intent(this, ProductActivity.class);
        productIntent.putExtra("text", tv.getText());
        startActivity(productIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_product_setting:
                String format = getString(R.string.product_name_format);
                ll.addView(new Product(this, String.format(format, counter)));
                counter++;
                return true;
            default:
                return true;
        }
    }
}
