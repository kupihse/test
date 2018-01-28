package com.example.services;


import com.example.models.Product;
import com.example.models.User;

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
        Call<Void> add(@Body Product product);

        @GET("pr/all")
        Call<List<Product>> getAll();

        @GET("pr/id/{id}")
        Call<Product> get(@Path("id") String id);
    }
    // Сервис запросов вкладок логина/регистрации
    public interface UserService {
        @POST("/user/log")
        Call<Void> log(@Body User user);

        @POST("/user/new")
        Call<Void> add(@Body User user);

    }

    public static class SendableImage {
        public String id, body;
    }

    public interface ImageService {
        @POST("/images/add")
        Call<Void> add(@Body SendableImage img);

        @GET("/images/get/{id}")
        Call<SendableImage> get(@Path("id") String id);
    }

    // Создаем объект ретрофита
    // Добавляем автоматический конвертер в JSON и обратно (мы ж ленивые, да и зачем руками это делать)
    // (потенциально руками будет быстрее работать, будет место для оптимизаций)
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://51.15.92.91")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Создаем сервисы запросов, описаные выше
    public static ProductService products = retrofit.create(ProductService.class);
    public static UserService users = retrofit.create(UserService.class);
    public static ImageService images = retrofit.create(ImageService.class);

    // Пустой коллбек – затычка, если надо отправить запрос и забить на него
    // (null передавать вместо него нельзя, кидает exception)
    static Callback<Void> emptyCallBack = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {}

        @Override
        public void onFailure(Call<Void> call, Throwable t) {}
    };
}
