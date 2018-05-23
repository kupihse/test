package com.example.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.application.R;
import com.example.events.LanguageChangeEvent;
import com.example.events.LayoutChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

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
                EventBus.getDefault().post(new LayoutChangeEvent(isListOn));
                if (isListOn) {
                    Toast.makeText(getContext(), "List View ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "List View OFF", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        findPreference("lang_eng").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isRuOn = (Boolean) newValue;
                EventBus.getDefault().post(new LanguageChangeEvent(isRuOn));
                if (isRuOn) {
                    Toast.makeText(getContext(), "Russian language ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Russian language OFF", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}