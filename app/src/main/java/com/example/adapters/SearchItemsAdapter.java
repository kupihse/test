package com.example.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.layouts.ProductLayout;
import com.example.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreyko0 on 22/02/2018.
 */

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.ViewHolder> {

    private List<Product> products;
    public ScrollingItemsAdapter.OnItemClickListener onItemClickListener;

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

    public void setOnItemClickListener(ScrollingItemsAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductLayout productLayout;

        public ViewHolder(ProductLayout layout) {
            super(layout);
            productLayout = layout;
        }

        public void setProductData(final Product p, final ScrollingItemsAdapter.OnItemClickListener listener) {
                productLayout.setProduct(p);
                if (listener == null) {
                    return;
                }

                productLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(p);
                    }
                });

                productLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onItemLongClick(p);
                        return true;
                    }
                });
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
        holder.setProductData(p, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
