package com.example.services;

import com.example.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Andreyko0 on 28/01/2018.
 */
// Сервис запросов товаров
// что для чего можно понять из названий методов, и что они возвращают/принимают
public interface ProductService {
    @POST("pr/new")
    Call<Void> add(@Body Product product);

    @GET("pr/all")
    Call<List<Product>> getAll();

    @GET("pr/id/{id}")
    Call<Product> get(@Path("id") String id);

    @GET("pr/all/n/{start}/{n}")
    Call<NProductsResponse> getN(@Path("start") Integer start, @Path("n") Integer n);

    class NProductsResponse {
        public List<Product> products;
        public int max;
    }
}
