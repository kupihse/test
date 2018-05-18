package com.example.layouts;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.adapters.ScrollingItemsAdapter;
import com.example.application.R;
import com.example.models.Product;

import java.util.List;

public class ProductListView {

    ScrollingItemsAdapter productAdapter;
    RecyclerView recyclerView;
    Context context;
    View rootView;

    SwipeRefreshLayout swipeRefreshLayout;

    Listeners listeners;

    public ProductListView(Context context, Listeners listeners, ScrollingItemsAdapter.OnItemClickListener itemClickListener) {
        rootView = LayoutInflater.from(context).inflate(R.layout.product_list_view, null);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);

        RecyclerView recyclerView = rootView.findViewById(R.id.products);

        this.recyclerView = recyclerView;
        this.context = context;
        this.listeners = listeners;
        setSwipeRefreshLayout(swipeRefreshLayout, listeners);
        setRecyclerViewLayout(context, recyclerView, swipeRefreshLayout, listeners, itemClickListener);
    }

    private void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, final Listeners listeners) {
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productAdapter.clear();
                listeners.refresh(mCallback);
            }
        });
    }


    private void setRecyclerViewLayout(Context context, final RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout, final Listeners listeners, ScrollingItemsAdapter.OnItemClickListener itemClickListener) {
        productAdapter = new ScrollingItemsAdapter();
        productAdapter.setOnUpdateListener(new ScrollingItemsAdapter.OnUpdateListener() {
            @Override
            public void onUpdate() {
                listeners.update(mCallback);
            }
        });

        productAdapter.setOnItemClickListener(itemClickListener);

//        recyclerView = root.findViewById(R.id.products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
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


    public View getView() {
        return rootView;
    }

    public interface Listeners {
        void update(ProductsCallback callback);

        void refresh(ProductsCallback callback);
    }

    public void scrollToPosition(int pos) {
        recyclerView.scrollToPosition(pos);
    }

    public void setLayoutViewType(final int viewType) {
        int pos = getFirstItemPosition();

        switch (viewType) {
            case ScrollingItemsAdapter.VIEW_LIST:
                recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_LIST);
                break;
            case ScrollingItemsAdapter.VIEW_GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(this.context, 2));
                productAdapter.setViewType(ScrollingItemsAdapter.VIEW_GRID);
                break;
        }

        recyclerView.setAdapter(productAdapter);
        recyclerView.scrollToPosition(pos);

    }

    public int getFirstItemPosition() {
        switch (productAdapter.viewType) {
            case ScrollingItemsAdapter.VIEW_LIST:
                return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            case ScrollingItemsAdapter.VIEW_GRID:
                return ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }
        return -1;
    }

    public void addProducts(List<Product> products, int maxProducts) {
        productAdapter.addProducts(products);
        if (productAdapter.getItemCount() == maxProducts) {
            productAdapter.setOnUpdateListener(null);
        }
    }

    public interface ProductsCallback {
        void onProducts(List<Product> products, int maxProducts);
    }

    private ProductsCallback mCallback = new ProductsCallback() {
        @Override
        public void onProducts(List<Product> products, int maxProducts) {
                            productAdapter.addProducts(products);
                if (productAdapter.getItemCount() == maxProducts) {
                    productAdapter.setOnUpdateListener(null);
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
        }
    };

    public void start() {
        listeners.update(mCallback);
    }
}
