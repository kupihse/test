package com.example.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.activities.AddProductActivity;
import com.example.adapters.ScrollingItemsAdapter;
import com.example.application.R;
import com.example.events.LayoutChangeEvent;
import com.example.models.Product;
import com.example.services.Services;
import com.example.util.Pair;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllProductsFragment extends Fragment {

    public ScrollingItemsAdapter productAdapter;
    public RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton buttonChangeView;

    int start = 0;
    int n_pr = 20;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_products, container, false);

        // Можно потом добавить меню в виде трех точек
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_all_items_fragment);
        toolbar.getMenu().findItem(R.id.download).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                download();
                return true;
            }
        });

        // На потом, надо сделать обновление по свайпу вниз
        //
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rerender(true);
            }
        });

        setRecyclerViewLayout(view);

        view.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });

        view.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivityForResult(new Intent(getContext(), AddProductActivity.class), 1);
                } else {
                    Toast.makeText(getContext(), "Ты не вошел в аккаунт, лох", Toast.LENGTH_SHORT).show();
                }

            }
        });


        buttonChangeView = view.findViewById(R.id.button_change_view);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isInListView = prefs.getBoolean("list_view", false);
        if (isInListView) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            productAdapter.setViewType(ScrollingItemsAdapter.VIEW_LIST);
            buttonChangeView.setImageResource(R.drawable.button_list);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            productAdapter.setViewType(ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID);
            buttonChangeView.setImageResource(R.drawable.button_grid);
        }
        buttonChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (productAdapter.viewType) {
                    case ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID:
                        int pos = getFirstItemPosition();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        productAdapter.setViewType(ScrollingItemsAdapter.VIEW_LIST);
                        buttonChangeView.setImageResource(R.drawable.button_list);
                        recyclerView.setAdapter(productAdapter);
                        recyclerView.scrollToPosition(pos);
                        return;
                    case ScrollingItemsAdapter.VIEW_LIST:
                        pos = getFirstItemPosition();
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        productAdapter.setViewType(ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID);
                        buttonChangeView.setImageResource(R.drawable.button_grid);
                        recyclerView.setAdapter(productAdapter);
                        recyclerView.scrollToPosition(pos);
                        return;

                }
            }
        });

        rerender(false);

        return view;
    }


    public int getFirstItemPosition() {
        switch (productAdapter.viewType) {
            case ScrollingItemsAdapter.VIEW_LIST:
                return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            case ScrollingItemsAdapter.VIEW_GRID:
            case ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID:
                return ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            case ScrollingItemsAdapter.VIEW_STAGGERED_GRID:
                return ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPositions(null)[0];
        }
        return -1;
    }


    private void setRecyclerViewLayout(View root) {
        productAdapter = new ScrollingItemsAdapter();
        productAdapter.setOnUpdateListener(new ScrollingItemsAdapter.OnUpdateListener() {
            @Override
            public void onUpdate() {
                renderMore(false);
            }
        });

        productAdapter.setOnItemClickListener(new ScrollingItemsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Product p) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.fragment_all_products_container, ProductFragment.newInstance(p.getId()))
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(Product product) {
                ProductPreviewFragment previewFragment = ProductPreviewFragment.newInstance(product.getId());
                getFragmentManager().beginTransaction()
                        .add(R.id.scrolling_activity_layout, previewFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView = root.findViewById(R.id.products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(productAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Обновляем только при отсутствии скролла (долистали до самого верха и листаем еще – тогда норм)
                swipeRefreshLayout.setEnabled(newState == RecyclerView.SCROLL_STATE_IDLE);
            }
        });
    }

    private void renderMore(final boolean showRefreshing) {
        if (showRefreshing)
            swipeRefreshLayout.setRefreshing(true);

        Toast.makeText(getActivity(), "REFRESH fr", Toast.LENGTH_SHORT).show();

        // делаем запрос на все товары
        Services.products.getN(start, n_pr).enqueue(new Callback<Pair<List<Product>, Integer>>() {
            @Override
            public void onResponse(Call<Pair<List<Product>, Integer>> call, Response<Pair<List<Product>, Integer>> response) {
                Pair<List<Product>, Integer> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null) {
                    return;
                }
                if (showRefreshing)
                    swipeRefreshLayout.setRefreshing(false);

                // Если что-то есть закидываем это в массив
//                productAdapter.addAll(prs);
                productAdapter.addProducts(prs.first);
                if (productAdapter.getItemCount() == prs.second) {
                    productAdapter.setOnUpdateListener(null);
                    return;
                }
                start += n_pr;
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<Pair<List<Product>, Integer>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    private void rerender(final boolean showRefreshing) {
        start = 0;
        productAdapter.clear();
        productAdapter.setOnUpdateListener(new ScrollingItemsAdapter.OnUpdateListener() {
            @Override
            public void onUpdate() {
                renderMore(false);
            }
        });
        renderMore(showRefreshing);
    }


    private void download() {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kupihse/test/raw/master/app/build/outputs/apk/debug/app-debug.apk"));
        startActivity(browserIntent);
    }


    @Subscribe
    public void OnLayoutChangeEvent(LayoutChangeEvent event) {
        if (event.isInListView()) {
            int pos = getFirstItemPosition();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            productAdapter.setViewType(ScrollingItemsAdapter.VIEW_LIST);
            buttonChangeView.setImageResource(R.drawable.button_list);
            recyclerView.setAdapter(productAdapter);
            recyclerView.scrollToPosition(pos);
        } else {
            int pos = getFirstItemPosition();
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            productAdapter.setViewType(ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID);
            buttonChangeView.setImageResource(R.drawable.button_grid);
            recyclerView.setAdapter(productAdapter);
            recyclerView.scrollToPosition(pos);
            return;

        }
    }

}
