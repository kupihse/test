package com.example.events;

public class ProductDeletedEvent {
    public final String productId;
    public ProductDeletedEvent(String pId) {
        productId = pId;
    }
}
