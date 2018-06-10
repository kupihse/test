package com.example.models;

import java.util.ArrayList;
import java.util.List;

// Единичный юзер
public class User {
    private String name;
    private String login;
    private String password;
    private ArrayList<String> products = new ArrayList<>();
    private List<String> wishlist = new ArrayList<>();

    private boolean confirmed;

    public User() {
        name = "";
        login = "";
        password = "";
    }

    public User(String login, String password) {
        name = "unknown";
        this.login = login;
        this.password = password;
    }

    public User(String name, String logname, String password) {
        this.name = name;
        this.login = logname;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotConfirmed() {
        this.confirmed = false;
    }

    public void confirm() {
        this.confirmed = true;
    }

    public void addProduct(String product) {
        products.add(product);
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public List<String> getWishlist(){ return wishlist; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        return password.equals(user.password);
    }
}
