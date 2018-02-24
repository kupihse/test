package com.example.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    final static private int TYPE_ITEM = 0;
    final static private int TYPE_BUTTON = 1;

    private List<Product> products;

//    private View.OnClickListener buttonListener;

    public OnUpdateListener onUpdateListener;

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

    public void setOnUpdateListener(OnUpdateListener listener) {
        onUpdateListener = listener;
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

        public int getType() {
            return productLayout == null ? TYPE_BUTTON : TYPE_ITEM;
        }

        public void setProductData(Product p) {
            productLayout.setProduct(p);
        }
    }

    @Override
    public ScrollingItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
//        if (viewType == TYPE_BUTTON) {
//            View buttonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button_load_more, parent, false);
//            return new ViewHolder(buttonView);
//        }
        return new ViewHolder(new ProductLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        switch (holder.getType()) {
//            case TYPE_BUTTON:
//                if (onUpdateListener != null) {
//                    holder.button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            onUpdateListener.onUpdate();
//                        }
//                    });
//                }
//                return;
//            case TYPE_ITEM:
        holder.setProductData(products.get(position));
//                return;
//        }

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


    public interface OnUpdateListener {
        void onUpdate();
    }
}
