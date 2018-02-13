package com.example.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;
import com.example.services.Services;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestSearchActivity extends AppCompatActivity {
    private ArrayList<Product> products = new ArrayList<>();
    private LinearLayout ll;

    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_test);
//        ActionBar bar = getActionBar();
//        if (bar != null) bar.setHomeButtonEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_test_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("kek");

        ll = (LinearLayout) findViewById(R.id.search_test_products);
        searchView = (MaterialSearchView) findViewById(R.id.search_test_search_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_test, menu);

        // Get the SearchView and set the searchable configuration
        setSearchSettings(menu.findItem(R.id.search_test_menu_search));

        return true;
    }

    private void setSearchSettings(MenuItem item) {
        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchView.setSuggestions(new String[]{newText});
                return true;
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }
}
