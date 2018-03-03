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

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.ViewHolder> {

    private List<Product> products;
    public ScrollingItemsAdapter.OnItemLongClickListener onLongClickListener;

    public SearchItemsAdapter() {
        this(new ArrayList<Product>());
    }

    public SearchItemsAdapter(List<Product> prs) {
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

    public void setOnItemLongClickListener(ScrollingItemsAdapter.OnItemLongClickListener listener) {
        onLongClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductLayout productLayout;

        public ViewHolder(ProductLayout layout) {
            super(layout);
            productLayout = layout;
        }

        public void setProductData(Product p) {
                productLayout.setProduct(p);
        }

        public void setOnItemLongClickListener(ScrollingItemsAdapter.OnItemLongClickListener listener, Product p) {
            productLayout.setLongClick(listener, p);
        }
    }

    @Override
    public SearchItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(new ProductLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product p = products.get(position);
        holder.setProductData(p);
        if (onLongClickListener != null ) {
            holder.setOnItemLongClickListener(this.onLongClickListener, p);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
