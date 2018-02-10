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

    // Создаем объект ретрофита
    // Добавляем автоматический конвертер в JSON и обратно (мы ж ленивые, да и зачем руками это делать)
    // (потенциально руками будет быстрее работать, будет место для оптимизаций)
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://51.15.92.91")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Создаем сервисы запросов, описаные выше
    public static ProductService products = retrofit.create(ProductService.class);
    public static UserService    users    = retrofit.create(UserService.class);
    public static ImageService   images   = retrofit.create(ImageService.class);
    public static LoggerService  logger   = retrofit.create(LoggerService.class);
    public static SearchService  search   = retrofit.create(SearchService.class);

    // Пустой коллбек – затычка, если надо отправить запрос и забить на него
    // (null передавать вместо него нельзя, кидает exception)
    public static Callback<Void> emptyCallBack = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
        }
    };
}
