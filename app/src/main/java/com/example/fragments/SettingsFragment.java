package com.example.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.application.R;

/**
 * Created by Andreyko0 on 03/03/2018.
 */

public class SettingsFragment extends PreferenceFragment {

//    public ScrollingItemsAdapter productAdapter;
//    private RecyclerView recyclerView;
//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
//        setHasOptionsMenu(true);
        SwitchPreference listSwitch = (SwitchPreference) findPreference("ListView");

        listSwitch.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                boolean isListOn = (Boolean) newValue;
                if (isListOn) {
                    Log.d("xzxzxzxz", "Switch is ON");
                }
                return true;
            }
        });
        }

}