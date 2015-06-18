package com.example.priyakarambelkar.checkinapplication;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SlackService {

    @POST("")
    void postSlackMEssage(@Body ServiceClass service, Callback<Void> callback);


}
