package com.example.andreyko0.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Andreyko0 on 30/09/2017.
 */

public class Product {
    private String name;
    private String description;
    private String id;
    private int price;
    transient ArrayList<Bitmap> images = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) { this.price = price; }

    public Bitmap getImage(int idx) {
        return images.get(idx);
    }

    public void setImage(ArrayList<Bitmap> image) { this.images = image; }

    public ArrayList<Bitmap> getImages() {
        return images;
    }
}
