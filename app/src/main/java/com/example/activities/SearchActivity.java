package com.example.activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;
import com.example.services.Services;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<Product> products = new ArrayList<>();
    private LinearLayout ll;


    // курсор адаптер – для подсказок поиска, без него не рабоатет
    // как работает он сам – я вообще не представляю
    // просто напхал чужого кода
    private CursorAdapter cursorAdapter;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar bar = getActionBar();
        if (bar != null) bar.setHomeButtonEnabled(true);

        ll = (LinearLayout) findViewById(R.id.search_activity_products);


        // здесь адаптер создаем
        cursorAdapter = buildProductSuggestionCursorAdapter(this);


        // Забираем данные интента приведшего нас сюда
        Intent intent = getIntent();
        // если это интент на обработку текста (когда выделяешь текст, появляется в контестном меню)
        // и если версия андроида позволяет соотв. интенты
        // то ставим text == пришедший из интента (выделенный в другом приложении)
        if (Build.VERSION.SDK_INT >= 23) {
            if (Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())) {
                text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
                return;
            }
        }

//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            text = intent.getStringExtra(SearchManager.QUERY);
//        } else

        // Забираем текст из интента
        // SearchManager.QUERY –– дефолтно, если из виджета поиска (я показывал раньше скрины)
        // Если наша активити, я все равно ставлю тот же параметр
        text = intent.getStringExtra(SearchManager.QUERY);
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
        for (Product p: products) {
            ll.addView(new ProductLayout(this, p));
        }
    }


    // задаем посик

    private void setSearchSettings(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();

        // ставим адаптер (он сам как-то все делает с подсказками
        // надо только курсор с данными передать и обновлять его
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setQueryHint("Ищем говно...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // если юзер хочет подтвердил запрос, идем на бэк по соотв. адресу и забираем список подходящих товаров
            // рерндерим списко товаров на уже фронте, так же, как и в ScrollingActivity
            @Override
            public boolean onQueryTextSubmit(final String s) {
                products.clear();

                Services.search.searchByName(s).enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        List<Product> prs = response.body();
                        // Если ничего не пришло, то ничего не делаем
                        if (prs == null) {
                            return;
                        }
                        // Если что-то есть закидываем это в массив
                        for(Product sp: prs) {
                            products.add(sp);
                        }
                        // Ну и ререндерим
                        renderItems();
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(SearchActivity.this, "src fail: "+s, Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                setSuggestions(cursorAdapter, s);
                return true;
            }
        });

        // если перешли в эту активити, значит уже готовы прям искатб
        // сразу расширяем строку поиска
        searchView.setIconified(false);
        // если есть текст при запросе в активити, ставим этот текск в строку поиска и подтвержадем (параметр true в setQuery)
        // выполняется запрос в OnQueryTextListener.onQueryTextSubmit  (выше)
        if (text != null) {
            searchView.setQuery(text, true);
            searchView.clearFocus();
        }
    }

    // Показываем подсказки, забрав данные с сервера
    // опять же хз как работают эти курсоры

    public static void setSuggestions(final CursorAdapter adapter, final String query) {
        final MatrixCursor cursor = new MatrixCursor(new String[]{ BaseColumns._ID, "productName" });

        // Идем на бэк, там по имени – возвращается список названий подходящих товаров
        Services.search.suggestNames(query).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> names = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (names == null) {
                    return;
                }
                // Если что-то есть закидываем это в массив
                for(int i = 0; i < names.size(); ++i) {
                    cursor.addRow(new Object[]{i, names.get(i)});
                }
                adapter.changeCursor(cursor);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {}
        });
    }



    // вообще не представляю что здесь что
    // но R.layout.suggesion_list_row –– это один ряд, соответственно можно свой сделать

    public static CursorAdapter buildProductSuggestionCursorAdapter(Context context) {
        return new SimpleCursorAdapter(context,
                R.layout.suggesion_list_row,
                null,
                new String[]{"productName"},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

}
