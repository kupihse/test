package com.example.andreyko0.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;

import com.example.application.R;

import java.util.ArrayList;

/**
 * Created by Andreyko0 on 30/09/2017.
 */

public class SendableProduct {
    public String name;
    public String description;
    public String id;
    public int price;
    public ArrayList<String> images = new ArrayList<String>();

    SendableProduct(String name) {
        this.name = name;
    }

    SendableProduct setDescription(String description) {
        this.description = description;
        return this;
    }

    SendableProduct setPrice(int price) {
        this.price = price;
        return this;
    }

    SendableProduct setId(String id) {
        this.id = id;
        return this;
    }

    SendableProduct addImage(String img) {
        this.images.add(img);
        return this;
    }

    Product toProduct() {
        Product p = new Product(this.name);
        p.setDescription(this.description);
        p.setId(this.id);
        p.setPrice(this.price);
        ArrayList<Bitmap> imgs = new ArrayList<>();
        for (String es:images) {
            byte[] decodedString = Base64.decode(es, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgs.add(decodedByte);
        }
        p.setImage(imgs);

        return p;
    }
}
