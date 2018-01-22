package com.example.andreyko0.myapplication;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.Response;


/**
 * Created by Andreyko0 on 20/01/2018.
 */

public class Services {
    // Service interfaces
    public interface ProductService {
//        "http://51.15.92.91/pr/new";
        @POST("pr/new")
        Call<Void> newProduct(@Body SendableProduct product);

        @GET("pr/all")
        Call<List<SendableProduct>> getAll();
    }


    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://51.15.92.91")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static ProductService productService = retrofit.create(ProductService.class);

    static Callback<Void> emptyCallBack = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {}

        @Override
        public void onFailure(Call<Void> call, Throwable t) {}
    };
}
