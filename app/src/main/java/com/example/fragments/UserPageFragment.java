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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.MainActivity;
import com.example.application.R;
import com.example.events.FavoriteEvent;
import com.example.events.LogInEvent;
import com.example.events.UserChangedEvent;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPageFragment extends Fragment {

    private FirebaseAuth mAuth;
    static User mUser;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView wishlistElementsCountView;

    public UserPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                EventBus.getDefault().post(new LogInEvent(false));
                EventBus.getDefault().post(new UserChangedEvent(null));
                FirebaseAuth.getInstance().signOut();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_user_page_container, new EntryFormFragment())
                        .commit();
                return true;
            }
        });

        Services.users.get(mAuth.getCurrentUser().getEmail()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUser(mAuth.getCurrentUser(), response.body(), rootView);
                EventBus.getDefault().post(new UserChangedEvent(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "WTF 2", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    private void setUser(final FirebaseUser firebaseUser, final User myuser, final View rootView) {
        if (firebaseUser == null) {
            Toast.makeText(getContext(), "WTF 3, not in firebase", Toast.LENGTH_LONG).show();
            return;
        }
        if (myuser == null) {
            Toast.makeText(getContext(), "WTF 3, not in database", Toast.LENGTH_LONG).show();
            return;
        }

        mUser = myuser;

        wishlistElementsCountView = rootView.findViewById(R.id.number_of_wishlist);
        final TextView goodsCountView = rootView.findViewById(R.id.number_of_goods);
        final TextView loginView = rootView.findViewById(R.id.user_page_login);
        loginView.setText(myuser.getLogin());

        wishlistElementsCountView.setText(String.valueOf(myuser.getWishlist().size()));
        Services.products.getAddedBySellerId(myuser.getLogin()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                goodsCountView.setText(String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
//                wishlistElementsCountView.setText(String.valueOf(WishList.getInstance().size()));
//                goodsCountView.setText(String.valueOf(myuser.getProducts().size()));
//                loginView.setText(myuser.getLogin());
                Services.users.get(mAuth.getCurrentUser().getEmail()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        setUser(mAuth.getCurrentUser(), response.body(), rootView);
                        EventBus.getDefault().post(new UserChangedEvent(response.body()));
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getContext(), "WTF 2", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        TextView userNameView = rootView.findViewById(R.id.user_page_name);
        userNameView.setText(myuser.getName());

        // todo wtf? не совсем понятно, что проверяется
        String currentUserLogin = mAuth.getCurrentUser().getEmail();
        if (mAuth.getCurrentUser() == null || !firebaseUser.getEmail().equals(currentUserLogin)) {
            return;
        }

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", myuser.getLogin(),
                        null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        final ImageView mailImageView = rootView.findViewById(R.id.mail_image);
        mailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", myuser.getLogin(),
                        null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });


        // todo get title from resources
        final View myProductsView = rootView.findViewById(R.id.my_products);
        myProductsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.fragment_user_page_container, ProductListFragment.newInstance("pr/sellerId/"+mUser.getLogin(), getResources().getString(R.string.added_products)))
                        .addToBackStack(null)
                        .commit();
            }
        });

        rootView.findViewById(R.id.number_of_wishlist_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction()
                        .add(R.id.fragment_user_page_container, ProductListFragment.newInstance("pr/sellerId/"+mUser.getLogin()+"/wishlist", getResources().getString(R.string.your_wishlist)))
                        .addToBackStack(null)
                        .commit();
            }
        });



//        Button myProducts = (Button) root.findViewById(R.id.user_page_products);
//        myProducts.setVisibility(View.VISIBLE);

    }


    public void myProductsButton(View v) {
        for (String p : mUser.getProducts()) {
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

    // todo мб поменять чтобы реально обновляло? а не выставляло те же по сути значения

//    public static void refreshInfo(final View rootView) {
////        final TextView wishlistElementsCount = (TextView) root.findViewById(R.id.number_of_wishlist);
////        final TextView goodsCount = (TextView) root.findViewById(R.id.number_of_goods);
////        final TextView login = root.findViewById(R.id.user_page_login);
////        login.setText(mUser.getLogin());
////        wishlistElementsCount.setText(String.valueOf(WishList.getInstance().size()));
////        goodsCount.setText(String.valueOf(mUser.getProducts().size()));
//
//        Services.users.get(FirebaseAuth.getInstance().getCurrentUser().getEmail()).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                setUser(FirebaseAuth.getInstance().getCurrentUser(), response.body(), rootView);
//                EventBus.getDefault().post(new UserChangedEvent(response.body()));
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(getContext(), "WTF 2", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @Subscribe
    public void onFavoriteEvent(FavoriteEvent event) {
        int count = Integer.valueOf(wishlistElementsCountView.getText().toString());
        if (event.toFavorite()) {
            count++;
        } else {
            count--;
        }
        wishlistElementsCountView.setText(String.valueOf(count));

    }
}
