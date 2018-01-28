package com.example.services;

import com.example.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Andreyko0 on 28/01/2018.
 */

// Сервис запросов вкладок логина/регистрации
public interface UserService {
    @POST("/user/log")
    Call<Void> log(@Body User user);

    @POST("/user/new")
    Call<Void> add(@Body User user);

}
