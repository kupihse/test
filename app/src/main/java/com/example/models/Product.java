package com.example.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andreyko0 on 30/09/2017.
 */

public class Product {
    private long sendableDate;
    private String name;
    private String description;
    private String id;
    private Date currentDate;
    private int price;
    private Set<String> tags = new TreeSet<>();
    private ArrayList<String> images = new ArrayList<>();
    private String sellerId;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public long getSendableDate() {
        return sendableDate;
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

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void addImage(String id) {
        images.add(id);
    }

    public String getImage(int idx) {
        return images.get(idx);
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setSellerId(String id) {
        sellerId = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void addTag(String s) {
        tags.add(s);
    }

}
