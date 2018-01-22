package com.example.andreyko0.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.R;
import com.example.s1k0de.entry.EntryFormActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private LinearLayout ll;
    private ArrayList<Product> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll = (LinearLayout) findViewById(R.id.products);
        rerender();

        //        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                rerender();
//            }
//        });
    }

    private void renderItems() {
        Log.d("RERENDER ITEMS", "items");
        if (ll == null) {
            return;
        }
        ll.removeAllViews();
        if (products.isEmpty()) {
            Log.d("RERENDER", "EMPTY");
            return;
        }
        Log.d("RERENDER", String.format("%d", products.size()));
        for (Product p: products) {
            Log.d("RERENDER ITEM", p.getName());
            ll.addView(new ProductLayout(this, p));
        }
    }

    private void rerender() {
        products.clear();
        Services.productService.getAll().enqueue(new Callback<List<SendableProduct>>() {
            @Override
            public void onResponse(Call<List<SendableProduct>> call, Response<List<SendableProduct>> response) {
                Log.d("RERENDER SUCCESS", "rndr");
                for(SendableProduct sp: response.body()) {
                    Log.d("RERENDER SINGLE", "rndr");
                    products.add(sp.toProduct());
                }
                renderItems();
            }

            @Override
            public void onFailure(Call<List<SendableProduct>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    public void doClick(View v) {
        TextView tv =  v.findViewById(R.id.product_id);
        Intent productIntent = new Intent(this, ProductActivity.class);
        productIntent.putExtra("item_id", tv.getText());
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
            case R.id.scrolling_menu_settings:
                return true;
            case R.id.scrolling_menu_add_product:
                startActivityForResult(new Intent(this, AddProductActivity2.class), 1);
//                String format = getString(R.string.product_name_format);
                return true;
            case R.id.scrolling_menu_reg:
                startActivity(new Intent(this, EntryFormActivity.class));
                return true;
            case R.id.scrolling_menu_refresh:
                rerender();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                rerender();
            }
        }
    }
}
