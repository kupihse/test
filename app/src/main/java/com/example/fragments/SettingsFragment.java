package com.example.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
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
        final PreferenceManager preferenceManager = getPreferenceManager();

        boolean checkLangSwitch = preferenceManager.getSharedPreferences().getBoolean("lang_rus", false);
        boolean checkViewSwitch = preferenceManager.getSharedPreferences().getBoolean("list_view", false);
        EventBus.getDefault().post(new LayoutChangeEvent(checkViewSwitch));
        EventBus.getDefault().post(new LanguageChangeEvent(checkLangSwitch));

        String lang;
        String currLang = Locale.getDefault().getLanguage();
        if (checkLangSwitch) {
            lang = "ru";
        } else {
            lang = "en";
        }

//        if (!currLang.equals(lang)) {
////            EventBus.getDefault().post(new LanguageChangeEvent(preferenceManager.getSharedPreferences().getBoolean("list_view", false)));
//            Log.d("lang_test", Locale.getDefault().getLanguage());
//        }


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

        findPreference("lang_rus").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
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