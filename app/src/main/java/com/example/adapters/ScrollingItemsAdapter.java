package com.example.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreyko0 on 22/02/2018.
 */

public class ScrollingItemsAdapter extends RecyclerView.Adapter<ScrollingItemsAdapter.ViewHolder> {

    final static public int VIEW_LIST = 0;
    final static public int VIEW_GRID = 1;

    private List<Product> products;
    public int viewType = VIEW_GRID;

    public OnUpdateListener onUpdateListener;
    public OnItemClickListener onItemClickListener;

    public ScrollingItemsAdapter() {
        this(new ArrayList<Product>());
    }

    public Product getItem(int i) {
        return products.get(i);
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

    public void addProduct(Product p) {
        products.add(p);
        notifyDataSetChanged();
    }

    public void removeProduct(String id) {
        for(int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.remove(i);
                break;
            }
        }
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
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
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

        public void setProductData(final Product p, final OnItemClickListener listener) {
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
    public ScrollingItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        switch (this.viewType) {
            case VIEW_LIST:
                return new ViewHolder(new ProductLayout(parent.getContext()));
            case VIEW_GRID:
                return new ViewHolder(new ProductLayout(parent.getContext(),
                        R.layout.single_product_transparent_grid));
        }
        return new ViewHolder(new ProductLayout(parent.getContext()));
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        EventBus.getDefault().unregister(holder.productLayout);
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product p = products.get(position);
        holder.setProductData(p, onItemClickListener);
        //         1 из вариантов подгрузки при пролистывании вниз
        if (onUpdateListener != null && position == products.size()-1) {
            onUpdateListener.onUpdate();
        }
        EventBus.getDefault().register(holder.productLayout);
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

    public interface OnItemClickListener {
        void onItemClick(Product p);
        void onItemLongClick(Product p);
    }
}
