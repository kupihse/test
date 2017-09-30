package com.example.andreyko0.myapplication;

import java.util.UUID;

/**
 * Created by Andreyko0 on 01/10/2017.
 */

public class User {
    private static String email;
    private static UUID id;
    private static boolean authenticated = false;


    public static void setEmail(String e) {
        id = UUID.randomUUID();
        email = e;
    }

    public static String getEmail() {
        return email;
    }

    public static UUID getId() {
        return id;
    }

    public static void authenticate(String email, String pass) {
        authenticated = false;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }

 }
