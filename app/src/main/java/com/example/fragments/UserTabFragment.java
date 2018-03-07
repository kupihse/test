package com.example.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.storages.CurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTabFragment extends Fragment {


    public UserTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_tab, container, false);

        if (!CurrentUser.isSet()) {
            Toast.makeText(getContext(), "Not set", Toast.LENGTH_SHORT).show();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_user_tab_container, new EntryFormFragment())
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_user_tab_container, new UserPageFragment())
                    .commit();
        }
        return root;
    }
}
