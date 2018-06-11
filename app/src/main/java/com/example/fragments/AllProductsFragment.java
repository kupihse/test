package com.example.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.AddProductActivity;
import com.example.adapters.ScrollingItemsAdapter;
import com.example.application.R;
import com.example.events.LanguageChangeEvent;
import com.example.events.LayoutChangeEvent;
import com.example.layouts.ProductListView;
import com.example.models.Product;
import com.example.services.Services;
import com.example.util.Pair;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductsFragment extends Fragment {
    ProductListView productListView;

    int start = 0;
    int n_pr = 20;
//    View mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().post(new LayoutChangeEvent(checkViewSwitch));
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_products, container, false);
//        mainView = getActivity().findViewById(R.id.scrolling_activity_layout);

        // Можно потом добавить меню в виде трех точек
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_all_items_fragment);
        toolbar.getMenu().findItem(R.id.download).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                download();
                return true;
            }
        });

        // На потом, надо сделать обновление по свайпу вниз
        //

        productListView = new ProductListView(getContext(), new ProductListView.Listeners() {
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
                        .add(R.id.fragment_all_products_container, ProductFragment.newInstance(p.getId()))
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

        FrameLayout layout = view.findViewById(R.id.products_view_content);
        layout.addView(productListView.getView());
//        setRecyclerViewLayout(view);

        view.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productListView.scrollToPosition(0);
            }
        });

        view.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivityForResult(new Intent(getContext(), AddProductActivity.class), 1);
                } else {
                    showAlert("Вы не вошли в аккаунт");
                }

            }
        });


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        boolean isInListView = prefs.getBoolean("list_view", false);

//        renderMore(false);
//        rerender(false);
        productListView.start();
        return view;
    }

    private void renderMore(final ProductListView.ProductsCallback callback) {

        // делаем запрос на все товары
        Services.products.getN(start, n_pr).enqueue(new Callback<Pair<List<Product>, Integer>>() {
            @Override
            public void onResponse(Call<Pair<List<Product>, Integer>> call, Response<Pair<List<Product>, Integer>> response) {
                Pair<List<Product>, Integer> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null) {
                    return;
                }
                // Если что-то есть закидываем это в массив
//                productAdapter.addAll(prs);
                callback.onProducts(prs.first, prs.second);
//                productAdapter.addProducts(prs.first);
//                if (productAdapter.getItemCount() == prs.second) {
//                    productAdapter.setOnUpdateListener(null);
//                    return;
//                }
                start += n_pr;
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<Pair<List<Product>, Integer>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }

    private void download() {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kupihse/test/raw/master/app/build/outputs/apk/debug/app-debug.apk"));
        startActivity(browserIntent);
    }


    @Subscribe
    public void OnLayoutChangeEvent(LayoutChangeEvent event) {
        if (event.isInListView()) {
            productListView.setLayoutViewType(ScrollingItemsAdapter.VIEW_LIST);
            Log.d("switchtest", "view ON");
        } else {
            productListView.setLayoutViewType(ScrollingItemsAdapter.VIEW_GRID);
            Log.d("switchtest", "view OFF");
        }
    }

    @Subscribe
    public void OnLanguageChangeEvent(LanguageChangeEvent event) {
        Locale locale;
        if (event.isLanguageRussian()) {
            locale = new Locale("ru");
            Log.d("switchtest", "language ON");
        } else {
            locale = new Locale("en");
            Log.d("switchtest", "language OFF");
        }

        String lang;
        String currLang = Locale.getDefault().getLanguage();
        if (event.isLanguageRussian()) {
            lang = "ru";
        } else {
            lang = "en";
        }
        if (!currLang.equals(lang)) {
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
            Log.d("lang_test", Locale.getDefault().getLanguage());

//            Intent intent = getActivity().getIntent();
//            getActivity().overridePendingTransition(0, 0);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            getActivity().finish();
//            getActivity().overridePendingTransition(0, 0);
//            startActivity(intent);

            getActivity().finish();
            getActivity().overridePendingTransition( 0, 0);
            getActivity().startActivity(getActivity().getIntent());

        }

//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getResources().updateConfiguration(config,
//                getResources().getDisplayMetrics());
//        Log.d("lang_test", Locale.getDefault().getDisplayLanguage());
//        getActivity().finish();
//        getActivity().overridePendingTransition( 0, 0);
//        getActivity().startActivity(getActivity().getIntent());
//        getActivity().overridePendingTransition( 0, 0);
    }

    private void showAlert(CharSequence notificationText) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        View mView = getLayoutInflater().inflate(R.layout.activity_popupwindow, null);
        Button button_popup = mView.findViewById(R.id.button_popup);
        TextView text = mView.findViewById(R.id.popupwindows);
        text.setText(notificationText);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        button_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}
