package com.example.andreyko0.myapplication;

/**
 * Created by Andreyko0 on 30/09/2017.
 */

public class Product {
    private String name;
    private String desctiption;
    private String id;

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, String desctiption) {
        this.name = name;
        this.desctiption = desctiption;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
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
}
