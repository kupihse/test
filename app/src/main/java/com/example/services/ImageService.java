package com.example.services;

import com.example.models.SendableImage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Andreyko0 on 28/01/2018.
 */

public interface ImageService {
    @POST("/images/add")
    Call<Void> add(@Body SendableImage img);

    @GET("/images/get/{id}")
    Call<SendableImage> get(@Path("id") String id);
}
