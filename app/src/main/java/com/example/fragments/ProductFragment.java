package com.example.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.HSEOutlet;
import com.example.activities.FullScreenImageActivity;
import com.example.activities.UserPageActivity;
import com.example.application.R;
import com.example.events.ProductDeletedEvent;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.ImageStorage;
import com.example.util.Pair;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
        final LinearLayout ll = root.findViewById(R.id.photos_2);
        // Делаем запрос по id
        Services.products.get(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                // Если все плохо, показываем тост (за здоровье сервера)
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_LONG).show();
                    return;
                }
                product = response.body();
                // Если товар не найден на сервере, то ВТФ?? как так-то
                // должен всегда возвращать, но на всякий случай
                if (product == null) {
                    Toast.makeText(getContext(), "No such item found", Toast.LENGTH_LONG).show();
                    return;
                }

                Services.users.get(product.getSellerId()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() == null) {
                            return;
                        }

                        final TextView sellerName = root.findViewById(R.id.product_user_login);
                        sellerName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), UserPageActivity.class);
                                intent.putExtra(UserPageActivity.USER_ID, product.getSellerId());
                                startActivity(intent);
                            }
                        });
                        sellerName.setText(response.body().getName());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });

                // Тут все очевидно на мой взгляд, и я устал уже писать все это говно
                TextView title = root.findViewById(R.id.title);
                title.setText(product.getName());

                TextView textView = root.findViewById(R.id.description_text);
                if (!product.getDescription().equals("")) {
                    textView.setText(product.getDescription());
                }
                else {
                    textView.setText(getResources().getString(R.string.empty_description));
                }


                String priceNum = Integer.toString(product.getPrice()) + " " + getResources().getString(R.string.rub);
                TextView price = root.findViewById(R.id.price_text);
                try {
                    price.setText(priceNum);
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(),"NULL", Toast.LENGTH_SHORT).show();
                }


                final LinearLayout tagList = root.findViewById(R.id.tag_list);
                if (product.getTags() != null) {
                    for (String tag : product.getTags()) {
                        TextView newTag = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                        newTag.setText(tag);
                        tagList.addView(newTag);
                    }
                }

                HorizontalScrollView scroll = root.findViewById(R.id.scroll);


                if (product.getImages() == null || product.getImages().size() == 0)
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
                        ll.findViewWithTag(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int n = (int) v.getTag();

                                // Переход на FullScreenImageActivity
                                Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);

                                intent.putExtra("Bitmap", product.getImages());
                                intent.putExtra("position", n);

                                // Передаем в FullScreenImageActivity bitmap картинки и стартуем
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    scroll.setVisibility(View.GONE);
                }

                Button writeToUser = root.findViewById(R.id.write_to_user);
                writeToUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_product_container, Chat.newInstance(product.getSellerId()))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_LONG).show();
            }
        });

//        String seller_id = product.getSellerId();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            root.findViewById(R.id.product_bought).setVisibility(View.GONE);
            return root;
        }

        Services.products.getProducts("pr/sellerId/" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), 0, 10)
                .enqueue(new Callback<Pair<List<Product>, Integer>>() {
                    @Override
                    public void onResponse(Call<Pair<List<Product>, Integer>> call, Response<Pair<List<Product>, Integer>> response) {
                        boolean found = false;
                        try {
                            for (Product p : response.body().first) {
                                if (p.getId().equals(product.getId())) {
                                    found = true;
                                    break;
                                }
                            }
                        } catch (NullPointerException e) {
                        }



                        View bought = root.findViewById(R.id.product_bought);
                        if (found) {
                            bought.setVisibility(View.VISIBLE);
                            root.findViewById(R.id.write_to_user).setVisibility(View.GONE);
                            bought.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Services.products.deleteById(product.getId()).enqueue(Services.emptyCallBack);
                                    showAlert(getResources().getString(R.string.deleted_products));
                                    EventBus.getDefault().post(new ProductDeletedEvent(product.getId()));
                                    getFragmentManager().popBackStack();
                                }
                            });
                        } else {
                            bought.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Pair<List<Product>, Integer>> call, Throwable t) {

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

    public void showPopUp(View v) {
//        String ViewId_Str = Integer.toString((Integer) v.getTag());
//        Integer idx = Integer.parseInt(ViewId_Str);
        int n = (int) v.getTag();

        // Переход на FullScreenImageActivity
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);

        intent.putExtra("Bitmap", product.getImages());
        intent.putExtra("position", n);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
//        intent.putExtra("Bitmap", product.getImage(n));

        startActivity(intent);
    }


    public void showAlert(CharSequence notificationText) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_popupwindow, null);
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
