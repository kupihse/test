package com.example.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.services.Services;
import com.example.layouts.ProductLayout;
import com.example.application.R;
import com.example.models.Product;
import com.example.s1k0de.entry.EntryFormActivity;
import com.example.storages.CurrentUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private LinearLayout ll;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> products_search = new ArrayList<>();
    MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Services.logger.sendLog("Started new scrolling activity").enqueue(Services.emptyCallBack);
        Log.d("START","SCROLL");
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recent items");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        // Тут пишем что происходит при закрытии поиска
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                rerender();
            }
        });

        // Тут сам поиск
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // При нажатии на поиск ищем (мб можно как-то лучше сделать процесс поиска)
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getName().contains(query)) {
                        products_search.add(products.get(i));
                    }
                }
                renderItems_search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Это для динамического поиска
                return false;
            }
        });

        ll = (LinearLayout) findViewById(R.id.products);
        rerender();


        // На потом, надо сделать обновление по свайпу вниз
        //
        //        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                rerender();
//            }
//        });
    }

    // Если делать через тот же метод и тот же список products, то работает хреново
    private void renderItems_search() {
        // если добавлять некуда, то зачем?
        if (ll == null) {
            return;
        }
        ll.removeAllViews();

        // Если добавлять неоткуда, то как?
        if (products_search.isEmpty()) {
            return;
        }
        // добавляем
        for (Product p: products_search) {
            ll.addView(new ProductLayout(this, p));
        }
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

    private void rerender() {
        //очищаем все товары, нам же не нужно дублировать
        // оптимизировать это потом ??
        products.clear();
        products_search.clear();

        // делаем запрос на все товары
        Services.products.getAll().enqueue(new Callback<List<Product>>() {
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

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    // КОСТЫЛЬ
    // В отедельном, неотображаемом TextView у нас записан id
    // забираем его и записываем в параметры запроса новой активити
    public void doClick(View v) {
        TextView tv =  v.findViewById(R.id.product_id);
        Intent productIntent = new Intent(this, ProductActivity.class);
        productIntent.putExtra("item_id", tv.getText());
        startActivity(productIntent);
    }

    // Просто создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        // если юзер сейчас есть, то кнопка входа не нужна
        // вместо нее ставим user page
        // если юзера нет, то наоборот
        if (CurrentUser.isLoggedIn()) {
            MenuItem item = menu.findItem(R.id.scrolling_menu_reg);
            item.setVisible(false);
            item = menu.findItem(R.id.scrolling_menu_user_page);
            item.setVisible(true);
        } else {
            MenuItem item = menu.findItem(R.id.scrolling_menu_reg);
            item.setVisible(true);
            item = menu.findItem(R.id.scrolling_menu_user_page);
            item.setVisible(false);
        }

        MenuItem item = menu.findItem(R.id.scrolling_menu_search);
        searchView.setMenuItem(item);
        return true;
    }

    // Нажали на кнопочку сверху справа (три точки)
    // Думаю тут все в целом понятно, просто switch по меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.scrolling_menu_settings:
                return true;
            case R.id.scrolling_menu_add_product:
                if (CurrentUser.isLoggedIn()) {
                    startActivityForResult(new Intent(this, AddProductActivity.class), 1);
                    return true;
                }
                else {
                    Toast.makeText(this, "Ты не вошел в аккаунт, лох", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.scrolling_menu_reg:
                startActivityForResult(new Intent(this, EntryFormActivity.class), 2);
                return true;
            case R.id.scrolling_menu_refresh:
                rerender();
                return true;

            // сделал реквест код, чтоб нормально ререндерить меню обратно
            case R.id.scrolling_menu_user_page:
                startActivityForResult(new Intent(this, UserPageActivity.class),2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // При удачном возврате из активити добавления товара, просто ререндерим
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                rerender();
            }
            if (requestCode == 2) {
                invalidateOptionsMenu();
            }
        }
    }
}
