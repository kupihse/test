package com.example.services;

import com.example.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Andreyko0 on 28/01/2018.
 */

// Сервис запросов вкладок логина/регистрации
public interface UserService {

    // Здесь также юзаем ResponseBody (пока что)
    @POST("/user/log")
    Call<String> log(@Body User user);

    @POST("/user/new")
    Call<Void> add(@Body User user);

    @GET("/user/get/")
    Call<User> get(@Query("id") String id);

}
