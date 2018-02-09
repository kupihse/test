package com.example.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;

public class SearchActivity extends AppCompatActivity {

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            text = intent.getStringExtra(SearchManager.QUERY);
//            doSearch(text);
        } else if (Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())) {
            text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
//            doSearch(text);
        }

        ActionBar bar = getActionBar();
        if (bar != null) bar.setHomeButtonEnabled(true);

    }

//    private void doSearch(String query) {
//        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
//
//        ((TextView) findViewById(R.id.search_activity_text)).setText(query);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        setSearchSettings(menu.findItem(R.id.search_menu_search));

        return true;
    }

    private void setSearchSettings(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Ищем говно...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        searchView.setIconified(false);
        if (text != null) {
            searchView.setQuery(text, true);
            searchView.clearFocus();
        }
    }
}
