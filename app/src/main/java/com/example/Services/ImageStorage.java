package com.example.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andreyko0 on 25/01/2018.
 */

public class ImageStorage {
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

    public static boolean isOnDisk(String id) {
        return false;
    }

    public static Bitmap getFromDisk(String id) {
        // #todo
        return null;
    }

    public static void setToDisk(String id, Bitmap img) {
        // #todo
    }

    public static void inject(ImageView view, String id) {
        Bitmap img = null;
        if (has(id)) {
            img = get(id);
        } else if (isOnDisk(id)) {
            img = getFromDisk(id);
        }


        if (img != null) {
            view.setImageBitmap(img);
        } else {
            final String imId = id;
            final ImageView imgPicture = view;
            Services.images.get(imId).enqueue(new Callback<Services.SendableImage>() {
                @Override
                public void onResponse(Call<Services.SendableImage> call, Response<Services.SendableImage> response) {
                    if (!response.isSuccessful()) {
                        // maybe #todo
                        return;
                    }
                    Services.SendableImage encImg = response.body();
                    if (encImg == null) {
                        // maybe #todo
                        return;
                    }
                    byte[] decodedString = Base64.decode(encImg.body, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    set(imId, decodedByte);
                    setToDisk(imId, decodedByte);
                    imgPicture.setImageBitmap(decodedByte);
                }

                @Override
                public void onFailure(Call<Services.SendableImage> call, Throwable t) {
                }
            });
        }
    }
}
