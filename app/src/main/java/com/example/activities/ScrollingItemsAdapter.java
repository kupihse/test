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



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, seller, price, date;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name  =  itemView.findViewById(R.id.product_text);
            seller = itemView.findViewById(R.id.product_user_login);
            price  =  itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.product_date);
            image = itemView.findViewById(R.id.ImageView);
        }

        public void setProductData(Product p) {
            ProductLayout.setProductsViews(name,seller,price,date,image,p);
        }
    }

    @Override
    public ScrollingItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setProductData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
