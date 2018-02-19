package com.example.storages;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andreyko0 on 01/02/2018.
 */

// В классе хранится текущий юзер, ну и все пока

public class CurrentUser {
    final public static String USER_PREF = "user_pref";

    private static String login;
    private static String token;
    private static SharedPreferences preferences;

    public static boolean isSet() {
        return login != null && token != null;
    }

    public static boolean isSaved() {
        return preferences != null
            && preferences.contains("login")
            && preferences.contains("token");
    }

    public static void save() {
        if (isSet()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login", login);
            editor.putString("token", token);
            editor.apply();
        }
    }

    public static void save(String login1, String token1) {
        setCredentials(login1, token1);
        save();
    }

    public static void logOut() {
        if (isSet()) {
            setCredentials(null, null);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("login");
            editor.remove("token");
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
                    preferences.getString("login", null),
                    preferences.getString("token", null));
        }
    }

    public static String getLogin() {
        return login;
    }

    public static String getToken() {
        return token;
    }

    public static void init(Context ctx) {
        preferences = ctx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        setFromDisk();
    }



}
