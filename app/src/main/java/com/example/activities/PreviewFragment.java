package com.example.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.R;
import com.example.layouts.ProductLayout;
import com.example.models.Product;

/**
 * Created by Andreyko0 on 27/02/2018.
 */

public class PreviewFragment extends DialogFragment {
    private Product product;

    public void setData(Product p) {
        product = p;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        getDialog().getWindow().setLayout((int) (metrics.widthPixels*0.7), (int) (metrics.heightPixels*0.7));
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rectangle_form);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_product_grid, container, false);
        TextView nameView  =  view.findViewById(R.id.product_text);
        TextView sellerName = view.findViewById(R.id.product_user_login);
        TextView priceView  =  view.findViewById(R.id.price);
        TextView dateView = view.findViewById(R.id.product_date);
        ImageView imgPicture = view.findViewById(R.id.ImageView);
        ImageView favoriteView = view.findViewById(R.id.image_favorite);
        ProductLayout.setProductsViews(nameView,sellerName,priceView,dateView,imgPicture, favoriteView, product, false);
         return view;
    }

}
