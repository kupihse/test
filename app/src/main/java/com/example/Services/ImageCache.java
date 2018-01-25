package com.example.Services;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Andreyko0 on 25/01/2018.
 */

public class ImageCache {
    private static Map<String, Bitmap> storage = new HashMap<>();

    public static String add(Bitmap img) {
        String id = UUID.randomUUID().toString();
        storage.put(id, img);
        return id;
    }

    public static void set(String id, Bitmap img) {
        storage.put(id, img);
    }

    public static Bitmap get(String id) {
        return storage.get(id);
    }

    public static void delete(String id) {
        storage.remove(id);
    }

    public static boolean has(String id) {
        return storage.containsKey(id);
    }
}
