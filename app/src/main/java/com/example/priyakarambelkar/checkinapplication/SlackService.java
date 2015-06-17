package com.example.priyakarambelkar.checkinapplication;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SlackService {

    @POST("/services/T026B13VA/B064U29MZ/vwexYIFT51dMaB5nrejM6MjK")
    void postSlackMEssage(@Body ServiceClass service, Callback<Void> callback);


}
