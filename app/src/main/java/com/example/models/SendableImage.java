package com.example.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Pair;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andreyko0 on 28/01/2018.
 */

public class SendableImage {
    public String id, body;

    public static SendableImage encode(String id, Bitmap bmp) {
        return SendableImage.encode(id, bmp, 100);
    }

    public static SendableImage encode(String id, Bitmap bmp, int quality) {
        SendableImage img = new SendableImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, quality, stream);
        String base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        img.id = id;
        img.body = base64;
        return img;
    }

    public Pair<String, Bitmap> decode() {
        byte[] decodedString = Base64.decode(this.body, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new Pair<>(this.id, bmp);
    }
}