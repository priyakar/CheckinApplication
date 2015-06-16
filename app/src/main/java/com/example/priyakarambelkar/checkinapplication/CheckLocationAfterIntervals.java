package com.example.priyakarambelkar.checkinapplication;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.TimerTask;

public class CheckLocationAfterIntervals extends TimerTask {
    Location lastLocation, currLocation;
    GoogleApiClient apiClient;
    double latIntrepid = 42.367266;
    double longIntrepid = -71.080130;
    private static int DISPLACEMENT = 50;
    float locationDifference = 0;
    public CheckLocationAfterIntervals (GoogleApiClient gApi ){
        this.apiClient = gApi;
        run();
    }
    @Override
    public void run() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (lastLocation != null) {
            currLocation.setLatitude(latIntrepid);
            currLocation.setLongitude(longIntrepid);
            locationDifference = lastLocation.distanceTo(currLocation);
            Log.e("curr location", lastLocation.getLatitude() + " " + lastLocation.getLongitude());
        if (locationDifference < DISPLACEMENT) {
            Log.e("location", "im within radius");
        }
        } else {
            Log.e("location", "in else of display");
        }
    }
}
