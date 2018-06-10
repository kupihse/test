package com.example.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.adapters.ScrollingItemsAdapter;
import com.example.application.R;
import com.example.layouts.ProductListView;
import com.example.models.Product;
import com.example.services.Services;
import com.example.util.Pair;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    ProductListView productListView;

    int start = 0;
    int n_pr = 20;


    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(String url, String title) {

        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title",title);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        // динамический title
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getArguments().getString("title"));

        final ProductListView productListView =  new ProductListView(getContext(), new ProductListView.Listeners() {
            @Override
            public void update(ProductListView.ProductsCallback callback) {
                renderMore(callback);
            }

            @Override
            public void refresh(ProductListView.ProductsCallback callback) {
                start = 0;
                renderMore(callback);

//                rerender(true);
            }
        }, new ScrollingItemsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Product p) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.fragment_products_list_container, ProductFragment.newInstance(p.getId()))
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(Product product) {
                ProductPreviewFragment previewFragment = ProductPreviewFragment.newInstance(product.getId());
                getFragmentManager().beginTransaction()
                        .add(R.id.scrolling_activity_layout, previewFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        rootView.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productListView.scrollToPosition(0);
            }
        });

        FrameLayout layout = rootView.findViewById(R.id.products_view_content);
        layout.addView(productListView.getView());
        Toast.makeText(getContext(), "Created product list", Toast.LENGTH_LONG).show();

        productListView.start();
        return rootView;
    }


    private void renderMore(final ProductListView.ProductsCallback callback) {
        Toast.makeText(getActivity(), "REFRESH fr", Toast.LENGTH_SHORT).show();

        // делаем запрос на все товары
        Services.products.getProducts(getArguments().getString("url"), start, n_pr).enqueue(new Callback<Pair<List<Product>, Integer>>() {
            @Override
            public void onResponse(Call<Pair<List<Product>, Integer>> call, Response<Pair<List<Product>, Integer>> response) {
                Toast.makeText(getContext(), "sent to "+call.request().url().toString(), Toast.LENGTH_SHORT).show();
                Log.d("URL//", call.request().url().toString());
                Pair<List<Product>, Integer> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null) {
                    Toast.makeText(getContext(), "Got nothing", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Got n "+prs.first.size(), Toast.LENGTH_SHORT).show();
                // Если что-то есть закидываем это в массив
                callback.onProducts(prs.first, prs.second);

                start += n_pr;
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<Pair<List<Product>, Integer>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }
}
