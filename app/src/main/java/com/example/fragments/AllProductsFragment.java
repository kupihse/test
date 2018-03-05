package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.activities.AddProductActivity;
import com.example.adapters.ScrollingItemsAdapter;
import com.example.application.R;
import com.example.models.Product;
import com.example.services.Services;
import com.example.storages.CurrentUser;
import com.example.util.Pair;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllProductsFragment extends Fragment {

    private ScrollingItemsAdapter productAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    int start = 0;
    int n_pr = 10;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_products, container, false);

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
        rerender(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all_items_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_all_items_fragment_add:
                if (CurrentUser.isSet()) {
                    startActivityForResult(new Intent(getActivity(), AddProductActivity.class), 1);
                    return true;
                } else {
                    Toast.makeText(getActivity(), "Ты не вошел в аккаунт, лох", Toast.LENGTH_SHORT).show();
                    return true;
                }

            case R.id.scrolling_menu_set_list:
                int pos = getFirstItemPosition();
                recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_LIST);
                recyclerView.setAdapter(productAdapter);
                recyclerView.scrollToPosition(pos);
                return true;
            case R.id.scrolling_menu_set_grid:
                pos = getFirstItemPosition();
                recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_GRID);
                recyclerView.setAdapter(productAdapter);
                recyclerView.scrollToPosition(pos);

                return true;
            case R.id.scrolling_menu_set_grid_2:
                pos = getFirstItemPosition();

                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_STAGGERED_GRID);
                recyclerView.setAdapter(productAdapter);
                recyclerView.scrollToPosition(pos);

                return true;
            case R.id.scrolling_menu_set_grid_3:
                pos = getFirstItemPosition();

                recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_TRANSPARENT_GRID);
                recyclerView.setAdapter(productAdapter);
                recyclerView.scrollToPosition(pos);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

        private int getFirstItemPosition() {
        switch (productAdapter.viewType) {
            case ScrollingItemsAdapter.VIEW_LIST:
                return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            case ScrollingItemsAdapter.VIEW_GRID:
                return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
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

        productAdapter.setOnItemLongClickListener(new ScrollingItemsAdapter.OnItemLongClickListener() {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Обновляем только при отсутствии скролла (долистали до самого верха и листаем еще – тогда норм)
                swipeRefreshLayout.setEnabled(newState == RecyclerView.SCROLL_STATE_IDLE);
            }
        });
//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.scrollToPosition(0);
//            }
//        });
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

}
