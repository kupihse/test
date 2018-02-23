package com.example.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.activities.entry.EntryFormActivity;
import com.example.application.R;
import com.example.models.Product;
import com.example.services.Services;
import com.example.storages.CurrentUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private ScrollingItemsAdapter productAdapter;

    int start = 0;
    int n_pr = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Services.logger.sendLog("Started new scrolling activity").enqueue(Services.emptyCallBack);
        Log.d("START", "SCROLL");
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HSE.Outlet");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));

        // На потом, надо сделать обновление по свайпу вниз
        //
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rerender(swipeRefreshLayout);
            }
        });

        final RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.products));
        recyclerView.setNestedScrollingEnabled(false);
        productAdapter = new ScrollingItemsAdapter();
        productAdapter.setOnUpdateListener(new ScrollingItemsAdapter.OnUpdateListener() {
            @Override
            public void onUpdate() {
                renderMore();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // Обновляем только при отсутствии скролла (долистали до самого верха и листаем еще – тогда норм)
                swipeRefreshLayout.setEnabled(newState == 0);

                // 1 из вариантов подгрузки при пролистывании вниз
//                if (!recyclerView.canScrollVertically(1) && newState == 0) {
//                    Toast.makeText(ScrollingActivity.this, "" + recyclerView.getBottom() + ":" + recyclerView.getVerticalScrollbarPosition(), Toast.LENGTH_SHORT).show();
//                    renderMore();
//                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        rerender();

    }

    private void download() {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kupihse/test/raw/master/app/build/outputs/apk/debug/app-debug.apk"));
        startActivity(browserIntent);
    }

    private void renderMore() {
        renderMore(null);
    }

    private void renderMore(final SwipeRefreshLayout srl) {
        if (srl != null)
            srl.setRefreshing(true);

        Toast.makeText(this, "REFRESH", Toast.LENGTH_SHORT).show();

        // делаем запрос на все товары
        Services.products.getN(start, n_pr).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null) {
                    return;
                }
                if (srl != null)
                    srl.setRefreshing(false);

                // Если что-то есть закидываем это в массив
//                productAdapter.addAll(prs);
                productAdapter.addProducts(prs);
                start += n_pr;
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    private void rerender(final SwipeRefreshLayout srl) {
        start = 0;
        productAdapter.clear();
        renderMore(srl);
    }

    private void rerender() {
        rerender(null);
    }

    // Просто создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        // если юзер сейчас есть, то кнопка входа не нужна
        // вместо нее ставим user page
        // если юзера нет, то наоборот
        if (CurrentUser.isSet()) {
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
                if (CurrentUser.isSet()) {
                    startActivityForResult(new Intent(this, AddProductActivity.class), 1);
                    return true;
                } else {
                    Toast.makeText(this, "Ты не вошел в аккаунт, лох", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.scrolling_menu_reg:
                startActivityForResult(new Intent(this, EntryFormActivity.class), 2);
                return true;
            case R.id.scrolling_menu_refresh:
                rerender();
                return true;
            case R.id.scrolling_menu_download:
                download();
                return true;

            // сделал реквест код, чтоб нормально ререндерить меню обратно
            case R.id.scrolling_menu_user_page:
                Intent intent = new Intent(this, UserPageActivity.class);
                intent.putExtra(UserPageActivity.USER_ID, CurrentUser.getLogin());
                startActivityForResult(intent, 2);
                return true;
            case R.id.scrolling_menu_search:
                startActivity(new Intent(this, SearchActivity.class));
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
                renderMore();
            }
            if (requestCode == 2) {
                invalidateOptionsMenu();
            }
        }
    }
}
