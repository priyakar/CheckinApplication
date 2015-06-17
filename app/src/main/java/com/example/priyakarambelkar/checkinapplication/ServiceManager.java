package com.example.priyakarambelkar.checkinapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;

public class ServiceManager  {
    private  static final Gson GSON = new GsonBuilder().create();
    private static RestAdapter adapter = new RestAdapter
            .Builder().setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint("https://hooks.slack.com/").build();
    private static SlackService slack;
    public static SlackService getSlackServiceInstatnce(){

        if (slack == null){
            slack = adapter.create(SlackService.class);
        }
        return slack;
    }

}
