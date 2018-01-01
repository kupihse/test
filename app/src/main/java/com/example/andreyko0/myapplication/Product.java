package com.example.andreyko0.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Andreyko0 on 30/09/2017.
 */

public class Product {
    private String name;
    private String description;
    private String id;
    private int price;
    Bitmap image;

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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) { this.image = image; }
}
