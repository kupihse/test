package com.example.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreyko0 on 22/02/2018.
 */

public class ScrollingItemsAdapter extends RecyclerView.Adapter<ScrollingItemsAdapter.ViewHolder> {


    private List<Product> products;

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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductLayout productLayout;

        public ViewHolder(ProductLayout layout) {
            super(layout);
            productLayout = layout;
        }

        public void setProductData(Product p) {
            productLayout.setProduct(p);
        }
    }

    @Override
    public ScrollingItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        return new ViewHolder(new ProductLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setProductData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
