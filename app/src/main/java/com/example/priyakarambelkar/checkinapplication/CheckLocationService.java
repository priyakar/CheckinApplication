package com.example.priyakarambelkar.checkinapplication;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient apiClient;
    public static boolean hasCheckedIn = false;
    private static int UPDATE_INTERVAL = 3000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.e("location", "play services 1");
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.e("location", "play services 2");
            } else {
                Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (apiClient != null) {
            apiClient.connect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (apiClient != null) {
            apiClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "connection failed" + connectionResult.getErrorCode());
    }

    void displayLocation() {
        CheckLocationAfterIntervals interval = new CheckLocationAfterIntervals(apiClient);
        Timer timer = new Timer();
        timer.schedule(interval, UPDATE_INTERVAL, 20000);
        if (CheckLocationAfterIntervals.chechIn && !hasCheckedIn) {
            ServiceClass p = new ServiceClass("Finally, I made it!", "Priya");
            ServiceManager.getSlackServiceInstatnce().postSlackMEssage(p, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    hasCheckedIn = true;
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
            timer.cancel();
            stopSelf();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "service destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
