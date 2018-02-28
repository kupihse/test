package com.example.activities;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.application.R;

/**
 * Created by Andreyko0 on 28/02/2018.
 */

public class PreviewTestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_preview, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PreviewTestFragment.this.getActivity(), "back", Toast.LENGTH_SHORT).show();
                PreviewTestFragment.this.getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.middle_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PreviewTestFragment.this.getActivity(), "middle", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PreviewTestFragment.this.getActivity(), "button", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
