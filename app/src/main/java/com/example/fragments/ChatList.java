package com.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.application.R;


public class ChatList extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_chat_list, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        LinearLayout ll = root.findViewById(R.id.user1);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_entry_form_container, new Chat())
                        .commit();
            }
        });
        Log.d("testtest", "testtest");
        return root;
    }
}