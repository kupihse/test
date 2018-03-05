package com.example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application.R;
import com.example.storages.CurrentUser;

/**
 * Created by Andreyko0 on 28/02/2018.
 */

public class ProductPreviewFragment extends Fragment {

    private int state;

    public static ProductPreviewFragment newInstance(String productId) {

        Bundle args = new Bundle();
        args.putString("id", productId);

        ProductPreviewFragment fragment = new ProductPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final String productId = getArguments().getString("id");

        View view = inflater.inflate(R.layout.layout_preview, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductPreviewFragment.this.getActivity(), "back", Toast.LENGTH_SHORT).show();
                ProductPreviewFragment.this.getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.middle_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductPreviewFragment.this.getActivity(), "middle", Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonText = view.findViewById(R.id.button);
        final ImageView buttonImage = view.findViewById(R.id.bookmark);
        if (!CurrentUser.isSet()) {
            // todo возможно поменять
            buttonText.setText("Add");
            buttonImage.setImageResource(R.drawable.bookmark_empty);
            state = 0;
        } else {

            if (CurrentUser.wishlist.contains(productId)) {
                buttonText.setText("Remove");
                buttonImage.setImageResource(R.drawable.bookmark);
                state = 1;
            } else {
                buttonText.setText("Add");
                buttonImage.setImageResource(R.drawable.bookmark_empty);
                state = 2;
            }

        }

        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (state) {
                    case 0:
                        Toast.makeText(getContext(), "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
                        return;
                    case 1:
                        buttonText.setText("Add");
                        buttonImage.setImageResource(R.drawable.bookmark_empty);
                        state = 2;
                        CurrentUser.wishlist.remove(productId);
                        return;
                    case 2:
                        buttonText.setText("Remove");
                        buttonImage.setImageResource(R.drawable.bookmark);
                        state = 1;
                        CurrentUser.wishlist.add(productId);
                        return;
                }
            }
        });

        return view;
    }
}
