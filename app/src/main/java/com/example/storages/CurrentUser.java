package com.example.storages;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andreyko0 on 01/02/2018.
 */

// В классе хранится текущий юзер, ну и все пока

public class CurrentUser {
    final private static String USER_PREF_KEY = "user_pref";
    final private static String TOKEN_KEY = "token";
    final private static String LOGIN_KEY = "login";
    final private static String WISHLIST_KEY = "wishlist";

    private static String login;
    private static String token;

    public static Set<String> wishlist;

    private static SharedPreferences preferences;

    public static boolean isSet() {
        return login != null && token != null;
    }

    public static boolean isSaved() {
        return preferences != null
                && preferences.contains(LOGIN_KEY)
                && preferences.contains(TOKEN_KEY);
    }

    public static void save() {
        if (isSet()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LOGIN_KEY, login);
            editor.putString(TOKEN_KEY, token);
            editor.putStringSet(WISHLIST_KEY, wishlist);
            editor.apply();
        }
    }

    public static void save(String login1, String token1) {
        setCredentials(login1, token1);
        wishlist = new HashSet<>();
        save();
    }

    public static void logOut() {
        if (isSet()) {
            setCredentials(null, null);
            wishlist = null;
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(LOGIN_KEY);
            editor.remove(TOKEN_KEY);
            editor.remove(WISHLIST_KEY);
            editor.apply();
        }
    }

    public static void setCredentials(String login1, String token1) {
        login = login1;
        token = token1;
    }

    public static void setFromDisk() {
        if (isSaved()) {
            setCredentials(
                    preferences.getString(LOGIN_KEY, null),
                    preferences.getString(TOKEN_KEY, null)
            );

            // Тут короче такая хрень, оказывается нельзя (или даже невозможно) модифицировать Se
            // возвращаемый через SharedPreferences.getStringSet
            // Поэтому оборачиваем в еще один хешсет
            wishlist = new HashSet<>(preferences.getStringSet(WISHLIST_KEY, new HashSet<String>()));
        }
    }

    public static String getLogin() {
        return login;
    }

    public static String getToken() {
        return token;
    }

    public static void init(Context ctx) {
        preferences = ctx.getSharedPreferences(USER_PREF_KEY, Context.MODE_PRIVATE);
        setFromDisk();
    }


}
