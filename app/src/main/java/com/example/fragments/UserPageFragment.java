package com.example.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.MainActivity;
import com.example.application.R;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPageFragment extends Fragment {

    private FirebaseAuth mAuth;
    static User user;
    private SwipeRefreshLayout swipeRefreshLayout;

    public UserPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_user_page, container, false);



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_user_page_fragment);
        toolbar.getMenu().findItem(R.id.log_out).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_user_page_container, new EntryFormFragment())
                        .commit();
                return true;
            }
        });


        final TextView login = rootView.findViewById(R.id.user_page_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", String.valueOf(login.getText()),
                        null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        final ImageView mailImage = rootView.findViewById(R.id.mail_image);
        mailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", String.valueOf(login.getText()),
                        null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        Services.users.get(mAuth.getCurrentUser().getEmail()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUser(mAuth.getCurrentUser(), response.body(), rootView);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "WTF 2", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void setUser(final FirebaseUser user, final User myuser, final View root) {
        if (user == null) {
            Toast.makeText(getContext(), "WTF 3", Toast.LENGTH_LONG).show();
            return;
        }
        this.user = myuser;

        final TextView wishlistElementsCount = (TextView) root.findViewById(R.id.number_of_wishlist);
        final TextView goodsCount = (TextView) root.findViewById(R.id.number_of_goods);
        final TextView login = root.findViewById(R.id.user_page_login);
        login.setText(myuser.getLogin());
        wishlistElementsCount.setText(String.valueOf(WishList.wishList.size()));
        goodsCount.setText(String.valueOf(myuser.getProducts().size()));

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                wishlistElementsCount.setText(String.valueOf(WishList.wishList.size()));
                goodsCount.setText(String.valueOf(myuser.getProducts().size()));
                login.setText(myuser.getLogin());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView token = (TextView) root.findViewById(R.id.user_page_name);
        token.setText(myuser.getName());
        String currentUserLogin = mAuth.getCurrentUser().getEmail();
        if (mAuth.getCurrentUser() == null || !user.getEmail().equals(currentUserLogin)) {
            return;
        }


//        Button myProducts = (Button) root.findViewById(R.id.user_page_products);
//        myProducts.setVisibility(View.VISIBLE);

    }


    public void myProductsButton(View v) {
        for (String p : user.getProducts()) {
            Toast.makeText(getContext(), p, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        ActionBar bar = ((MainActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Ваша страница");
        }

        if (getActivity().getActionBar() == null) {
            Toast.makeText(getContext(), "No bar", Toast.LENGTH_SHORT).show();
        }

        // todo set logout button
    }

    public static void refreshInfo(final View root) {
        final TextView wishlistElementsCount = (TextView) root.findViewById(R.id.number_of_wishlist);
        final TextView goodsCount = (TextView) root.findViewById(R.id.number_of_goods);
        final TextView login = root.findViewById(R.id.user_page_login);
        login.setText(user.getLogin());
        wishlistElementsCount.setText(String.valueOf(WishList.wishList.size()));
        goodsCount.setText(String.valueOf(user.getProducts().size()));
    }
}
