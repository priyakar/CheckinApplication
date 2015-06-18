package com.example.priyakarambelkar.checkinapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("YES".equals(action)) {
            Toast.makeText(context, "Notification Posted", Toast.LENGTH_SHORT).show();
            ServiceClass postMessage = new ServiceClass("Finally, I made it!", "Priya");
            ServiceManager.getSlackServiceInstatnce().postSlackMessage(BuildConfig.SLACK_CHANNEL_URL_KEY, postMessage, new Callback<Object>() {
                @Override
                public void success(Object aVoid, Response response) {
                    CheckLocationService.hasCheckedIn = true;
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Log.e("retrofit erroe", retrofitError.getCause().toString());
                }
            });
            CheckLocationService.notificationManager.cancel(0);
            Log.e("notify", "post msg yes");
        } else if ("NO".equals(action)) {
            CheckLocationService.notificationManager.cancel(0);
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            Log.e("notify", "post msg no");
        }
    }
}
