package com.example.storages;

/**
 * Created by Andreyko0 on 30/09/2017.
 */


// Локальный storage, в данный момент не используется
//public class ProductStorage {
//    static Map<String, SendableProduct> storage = new LinkedHashMap<>();
//
//    public static void addProduct(SendableProduct p) {
//        String id = UUID.randomUUID().toString();
//        p.setId(id);
//        storage.put(id, p);
//    }
//
//    public static SendableProduct getProduct(String id) {
//        return storage.get(id);
//    }
//
//    public static ArrayList<Product> getProducts() {
//        ArrayList<Product> products = new ArrayList<>();
//        for(SendableProduct sp: storage.values()) {
//            products.add(sp.toProduct());
//        }
//        return products;
//    }
//}
