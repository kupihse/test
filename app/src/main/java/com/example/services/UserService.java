package com.example.services;

import com.example.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<User> get(@Body String id);

}
