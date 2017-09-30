package com.example.andreyko0.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.R;

import org.w3c.dom.Text;

/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {
    public ProductLayout(Context ctx, Product p) {
        super(ctx);
        View v = inflate(getContext(), R.layout.single_product, this);
        String name = p.getName();
        TextView nameView  =  v.findViewById(R.id.product_text);
        nameView.setText(name);
        TextView idView = v.findViewById(R.id.product_id);
        idView.setText(p.getId());
    }
}
