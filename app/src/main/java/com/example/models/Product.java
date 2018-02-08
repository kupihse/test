package com.example.models;

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
    private ArrayList<String> images = new ArrayList<>();
    private String seller_id;

    public Product() {
    }
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

    public void addImage(String id) {
        images.add(id);
    }

    public String getImage(int idx) {
        return images.get(idx);
    }

    public void setImages(ArrayList<String> images) { this.images = images; }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setSellerId (String id) { seller_id = id; }

    public String getSellerId () { return seller_id; }

}
