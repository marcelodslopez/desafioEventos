package com.example.desafioeventos.api;

import com.example.desafioeventos.models.Checkin;
import com.example.desafioeventos.models.Event;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EventAPI {

    public static final String BASE_URL = "http://5b840ba5db24a100142dcd8c.mockapi.io/api/";


    @GET("events")
    Call<List<Event>> getEvents();

    @Headers("Content-Type: application/json")
    @POST("checkin")
    Call<String> sendChekin(@Body String body);



}
