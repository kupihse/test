package com.example.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.activities.entry.EntryFormActivity;
import com.example.adapters.MainViewPagerAdapter;
import com.example.application.R;
import com.example.fragments.AllProductsFragment;
import com.example.fragments.EmptySettingsFragment;
import com.example.fragments.SearchFragment;
import com.example.fragments.UserPageFragment;
import com.example.fragments.UserTabFragment;
import com.example.services.Services;
import com.example.storages.CurrentUser;

public class ScrollingActivity extends AppCompatActivity {

    private static final Fragment[] tabFragments = new Fragment[]{
        new AllProductsFragment(),
                new SearchFragment(),
                new UserTabFragment(),
                new EmptySettingsFragment()
    };

    private static final int[] tabIcons = new int[]{
            R.drawable.list,
            R.drawable.search,
            R.drawable.person,
            R.drawable.settings
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Services.logger.sendLog("Started new scrolling activity").enqueue(Services.emptyCallBack);
        Log.d("START", "SCROLL");
        setContentView(R.layout.activity_scrolling);

        ViewPager viewPager = findViewById(R.id.scrolling_viewpager);
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), tabFragments));
        viewPager.setOffscreenPageLimit(tabFragments.length);

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

    private void download() {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kupihse/test/raw/master/app/build/outputs/apk/debug/app-debug.apk"));
        startActivity(browserIntent);
    }


    // Просто создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        // если юзер сейчас есть, то кнопка входа не нужна
        // вместо нее ставим user page
        // если юзера нет, то наоборот
        if (CurrentUser.isSet()) {
            MenuItem item = menu.findItem(R.id.scrolling_menu_reg);
            item.setVisible(false);
            item = menu.findItem(R.id.scrolling_menu_user_page);
            item.setVisible(true);
        } else {
            MenuItem item = menu.findItem(R.id.scrolling_menu_reg);
            item.setVisible(true);
            item = menu.findItem(R.id.scrolling_menu_user_page);
            item.setVisible(false);
        }


        return true;
    }


//     Нажали на кнопочку сверху справа (три точки)
//     Думаю тут все в целом понятно, просто switch по меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.scrolling_menu_settings:
                return true;
            case R.id.scrolling_menu_reg:
                startActivityForResult(new Intent(this, EntryFormActivity.class), 2);
                return true;
//            case R.id.scrolling_menu_refresh:
//                rerender(false);
//                return true;
            case R.id.scrolling_menu_download:
                download();
                return true;

            // сделал реквест код, чтоб нормально ререндерить меню обратно
            case R.id.scrolling_menu_user_page:
                Intent intent = new Intent(this, UserPageActivity.class);
                intent.putExtra(UserPageActivity.USER_ID, CurrentUser.getLogin());
                startActivityForResult(intent, 2);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    // При удачном возврате из активити добавления товара, просто ререндерим
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                renderMore(false);
//            }
//            if (requestCode == 2) {
//                invalidateOptionsMenu();
//                productAdapter.notifyDataSetChanged();
//            }
//        }
//    }
}
