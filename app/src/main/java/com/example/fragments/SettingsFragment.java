package com.example.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.application.R;

/**
 * Created by Andreyko0 on 03/03/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        findPreference("list_view").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isListOn = (Boolean) newValue;
                if (isListOn) {
                    Toast.makeText(getContext(), "Switch on", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}