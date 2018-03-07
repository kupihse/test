package com.example.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Andreyko0 on 03/03/2018.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments;

    public MainViewPagerAdapter(final FragmentManager manager, final Fragment[] fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }
}
