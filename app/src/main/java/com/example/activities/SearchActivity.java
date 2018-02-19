package com.example.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;
import com.example.services.Services;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    final private static String SEARCH_HISTORY = "SEARCH_HISTORY";

    private ArrayList<Product> products = new ArrayList<>();
    private LinearLayout ll;

    private SharedPreferences preferences;
    private Set<String> history;

    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ll = (LinearLayout) findViewById(R.id.search_products);
        searchView = (MaterialSearchView) findViewById(R.id.search_search_view);
        setSearchSettings();

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchActivity.super.onBackPressed();
                }
            });
        }
        preferences = getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
        history = preferences.getStringSet(SEARCH_HISTORY, new HashSet<String>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        searchView.setMenuItem(menu.findItem(R.id.search_menu_search));
        searchView.showSearch(false);
        searchView.setSuggestions(history.toArray(new String[]{}));
        searchView.showSuggestions();
        return true;
    }

    private void setSearchSettings() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rerender(query);
                saveHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    // Рендерим товары, предварительно запоминаем их в массиве (надо ли?)
    private void renderItems() {
        // если добавлять некуда, то зачем?
        if (ll == null) {
            return;
        }
        ll.removeAllViews();

        // Если добавлять неоткуда, то как?
        if (products.isEmpty()) {
            return;
        }
        // добавляем
        for (Product p : products) {
            ll.addView(new ProductLayout(this, p));
        }
    }

    private void rerender(String text) {
        //очищаем все товары, нам же не нужно дублировать
        // оптимизировать это потом ??
        products.clear();
        Toast.makeText(this, "REFRESH", Toast.LENGTH_SHORT).show();

        // делаем запрос на все товары
        Services.search.searchByName(text).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null) {
                    return;
                }
                // Если что-то есть закидываем это в массив
                for (Product sp : prs) {
                    products.add(sp);
                }
                // Ну и ререндерим
                renderItems();
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    private void saveHistory(String text) {
        history.add(text);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(SEARCH_HISTORY, history);
        editor.apply();
    }

}
