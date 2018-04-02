package com.example.services;

import com.example.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Andreyko0 on 10/02/2018.
 */

public interface SearchService {

    @GET("/search/name/{name}")
    Call<List<Product>> searchByName(@Path("name") String name);

    @GET("/search/suggest/name/{name}")
    Call<List<String>> suggestNames(@Path("name") String name);

}

