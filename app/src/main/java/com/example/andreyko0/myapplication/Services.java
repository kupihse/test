package com.example.andreyko0.myapplication;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Andreyko0 on 20/01/2018.
 */

// Класс для запросов к серверу
public class Services {
    // Сервис запросов товаров
    // что для чего можно понять из названий методов, и что они возвращают/принимают
    public interface ProductService {
        @POST("pr/new")
        Call<Void> newProduct(@Body SendableProduct product);

        @GET("pr/all")
        Call<List<SendableProduct>> getAll();

        @GET("pr/id/{id}")
        Call<SendableProduct> getProduct(@Path("id") String id);
    }


    // Создаем объект ретрофита
    // Добавляем автоматический конвертер в JSON и обратно (мы ж ленивые, да и зачем руками это делать)
    // (потенциально руками будет быстрее работать, будет место для оптимизаций)
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://51.15.92.91")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Создаем сервис запросов товаров, описаный выше
    static ProductService productService = retrofit.create(ProductService.class);

    // Пустой коллбек – затычка, если надо отправить запрос и забить на него
    // (null передавать вместо него нельзя, кидает exception)
    static Callback<Void> emptyCallBack = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {}

        @Override
        public void onFailure(Call<Void> call, Throwable t) {}
    };
}
