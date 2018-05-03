package com.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.adapters.MainViewPagerAdapter;
import com.example.application.R;
import com.example.fragments.AllProductsFragment;
import com.example.fragments.ChatList;
import com.example.fragments.EntryFormFragment;
import com.example.fragments.SearchFragment;
import com.example.fragments.SettingsContainerFragment;
import com.example.fragments.UserPageFragment;
import com.example.models.Product;
import com.example.services.Services;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ViewPager viewPager;
    private AllProductsFragment allProductsFragment = new AllProductsFragment();

    private final Fragment[] tabFragments = new Fragment[]{
            allProductsFragment,
            new SearchFragment(),
            new ChatList(),
            null,
            new SettingsContainerFragment()
    };

    private static final int[] tabIcons = new int[]{
            R.drawable.list,
            R.drawable.search,
            R.drawable.chat,
            R.drawable.person,
            R.drawable.settings
    };


    // По нажатию на сенсорную кнопку назад, если есть возможность вернуть назад из текущего фрагмента,
    // то возвращаем его, иначе дефолтное действие
    @Override
    public void onBackPressed() {
        if (viewPager == null) {
            return;
        }
        Fragment curr = getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + viewPager.getCurrentItem());
        if (curr == null) {
            return;
        }

        FragmentManager fragmentManager = curr.getChildFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Services.logger.sendLog("Started new scrolling activity").enqueue(Services.emptyCallBack);
        Log.d("START", "SCROLL");
        setContentView(R.layout.activity_scrolling);

        viewPager = findViewById(R.id.scrolling_viewpager);

        if (mAuth.getCurrentUser() == null) {
            tabFragments[3] = new EntryFormFragment();
        }
        else {
            tabFragments[3] = new UserPageFragment();
        }

//        if (!CurrentUser.isSet()) {
//            tabFragments[2] = new EntryFormFragment();
//        } else {
//            tabFragments[2] = new UserPageFragment();
//        }
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), tabFragments));
        viewPager.setOffscreenPageLimit(tabFragments.length);

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = findViewById(R.id.tabs);
        if (tabLayout == null) {
            Log.d("WTF", "No TABS");
            Toast.makeText(this, "WTF NO TABS", Toast.LENGTH_SHORT).show();
            return;
        }
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabIcons.length; ++i) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }


    // При удачном возврате из активити добавления товара, просто ререндерим
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // todo мб переделать эту лестницу
                // todo и она чет не работает
                String id = data.getStringExtra("id");
                if (id != null) {
                    Services.products.get(id).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            Product product = response.body();
                            if (product != null) {
                                allProductsFragment.productAdapter.addProduct(product);
                            }
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {

                        }
                    });
                }
            }
        }
    }
}
