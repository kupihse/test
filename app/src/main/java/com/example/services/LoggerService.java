package com.example.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Andreyko0 on 29/01/2018.
 */

public interface LoggerService {
    @POST("/log/")
    Call<Void> sendLog(@Body String log);
}
