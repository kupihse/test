package com.example.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreyko0 on 22/02/2018.
 */

public class ScrollingItemsAdapter extends RecyclerView.Adapter<ScrollingItemsAdapter.ViewHolder> {

    final static public int VIEW_LIST = 0;
    final static public int VIEW_GRID = 1;
    final static public int VIEW_STAGGERED_GRID = 2;
    final static public int VIEW_TRANSPARENT_GRID = 3;

    private List<Product> products;
    public int viewType = VIEW_LIST;

    public OnUpdateListener onUpdateListener;
    public OnItemLongClickListener onLongClickListener;

    public ScrollingItemsAdapter() {
        this(new ArrayList<Product>());
    }

    public ScrollingItemsAdapter(List<Product> prs) {
        products = prs;
    }

    public void setProducts(List<Product> prs) {
        products = prs;
        notifyDataSetChanged();
    }

    public void addProducts(List<Product> prs) {
        products.addAll(prs);
        notifyDataSetChanged();
    }

    public void clear() {
        products.clear();
    }

    public void clearAndUpdate() {
        clear();
        notifyDataSetChanged();
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        onUpdateListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        onLongClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductLayout productLayout;
        public Button button;

        public ViewHolder(ProductLayout layout) {
            super(layout);
            productLayout = layout;
        }

        public ViewHolder(View view) {
            super(view);
            this.button = view.findViewById(R.id.button);
        }

        public void setProductData(Product p) {
                productLayout.setProduct(p);
        }

        public void setOnItemLongClickListener(OnItemLongClickListener listener, Product p) {
            productLayout.setLongClick(listener, p);
        }
    }

    @Override
    public ScrollingItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        switch (this.viewType) {
            case VIEW_LIST:
                return new ViewHolder(new ProductLayout(parent.getContext()));
            case VIEW_GRID:
                return new ViewHolder(new ProductLayout(parent.getContext(),
                        R.layout.single_product_grid));
            case VIEW_STAGGERED_GRID:
                ProductLayout layout = new ProductLayout(parent.getContext(),
                        R.layout.single_product_grid);
                layout.setHideImage(true);
                return new ViewHolder(layout);
            case VIEW_TRANSPARENT_GRID:
                return new ViewHolder(new ProductLayout(parent.getContext(),
                        R.layout.single_product_transparent_grid));
        }
        return new ViewHolder(new ProductLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product p = products.get(position);
        holder.setProductData(p);
        if (onLongClickListener != null ) {
            holder.setOnItemLongClickListener(this.onLongClickListener, p);
        }
        //         1 из вариантов подгрузки при пролистывании вниз
        if (onUpdateListener != null && position == products.size()-1) {
            onUpdateListener.onUpdate();
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position == products.size()) ? TYPE_BUTTON : TYPE_ITEM;
//    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    // Listeners

    public interface OnUpdateListener {
        void onUpdate();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Product p);
    }
}
