package com.example.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.FullScreenImageActivity;
import com.example.activities.ProductActivity;
import com.example.activities.UserPageActivity;
import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;
import com.example.services.Services;
import com.example.storages.ImageStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    private ImageView imgPicture;
    private Product product;


    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String id) {

        Bundle args = new Bundle();

        args.putString("id", id);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_product, container, false);

        String id = getArguments().getString("id");

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        root.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFullScreen();
            }
        });

        // Делаем запрос по id
        Services.products.get(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                // Если все плохо, показываем тост (за здоровье сервера)
                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(),"Failed to load", Toast.LENGTH_LONG).show();
                    return;
                }
                product = response.body();
                // Если товар не найден на сервере, то ВТФ?? как так-то
                // должен всегда возвращать, но на всякий случай
                if (product == null) {
                    Toast.makeText(getContext(),"No such item found", Toast.LENGTH_LONG).show();
                    return;
                }

                // Тут все очевидно на мой взгляд, и я устал уже писать все это говно
                TextView title = root.findViewById(R.id.title);
                title.setText(product.getName());

                TextView textView = root.findViewById(R.id.product_activity_text);
                textView.setText(product.getDescription() + "\n\n" + "Price: " + Integer.toString(product.getPrice()));

                final LinearLayout tagList = root.findViewById(R.id.tag_list);
                for (String tag : product.getTags()) {
                    TextView newTag = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                    newTag.setText(tag);
                    tagList.addView(newTag);
                }

                HorizontalScrollView scroll = root.findViewById(R.id.scroll);
                final LinearLayout ll = root.findViewById(R.id.photos_2);

                if (product.getImages() == null)
                    return;

                root.findViewById(R.id.no_images_text).setVisibility(View.GONE);
                imgPicture = root.findViewById(R.id.image);
                ImageStorage.inject(imgPicture, product.getImage(0));
                if (product.getImages().size() > 1) {
                    // если вообще есть дополнительные картинки
                    // идем по массиву и непосредственно добавляем в Layout
                    for (int i = 1; i < product.getImages().size(); i++) {
                        final String id = product.getImage(i);
                        SingleImageLayout Im = new SingleImageLayout(getContext(), id, i);
                        ll.addView(Im);
                    }
                } else {
                    scroll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(),"Failed to load", Toast.LENGTH_LONG).show();
            }
        });

//        String seller_id = product.getSellerId();
        TextView sellerName = (TextView) root.findViewById(R.id.product_user_login);
        sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserPageActivity.class);
                intent.putExtra(UserPageActivity.USER_ID, product.getSellerId());
                startActivity(intent);
            }
        });

        return root;
    }

    public void buttonFullScreen() {
        // Переход на FullScreenImageActivity
        Intent intent = new Intent(getContext(), FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
//        intent.putExtra("Bitmap", product.getImage(0));
//        Log.d("IMG LOG", "ASD");

        intent.putExtra("Bitmap", product.getImages());
        intent.putExtra("position", 0);

        startActivity(intent);
    }

}
