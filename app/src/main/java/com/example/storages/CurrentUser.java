package com.example.storages;

import com.example.models.User;

/**
 * Created by Andreyko0 on 01/02/2018.
 */

// В классе хранится текущий юзер, ну и все пока

public class CurrentUser {
    private static User user;
    private static String token;


    public static boolean isLoggedIn() {
        return !(user == null);
    }

    public static boolean hasToken() {
        return !(token == null);
    }

    public static void token(String tkn) {
        token = tkn;
    }

    public static String token() {
        return token;
    }

    public static User user() {
        return user;
    }

    public static void user(User u) {
        user = u;
    }

}
