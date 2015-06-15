package com.example.priyakarambelkar.checkinapplication;
import android.app.Service; import android.content.Intent; import android.location.Location; import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult; import com.google.android.gms.common.GooglePlayServicesUtil; import com.google.android.gms.common.api.GoogleApiClient; import com.google.android.gms.location.LocationRequest; import com.google.android.gms.location.LocationServices;
public class CheckLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final int PLAY_SERVICES_REQUEST = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Location lastLocation, currLocation; private GoogleApiClient apiClient;
    private boolean requestUpdates = false; private LocationRequest locationRequest; private static int UPDATE_INTERVAL = 300000; private static int DISPLACEMENT = 50;
    double latIntrepid = 42.367266; double longIntrepid = -71.080130; float locationDifference =0;
    @Override
    public IBinder onBind(Intent intent) { return null;
    }
    @Override
    public void onCreate() { super.onCreate();
        if (checkPlayServices()){
            buildGoogleApiClient();
            displayLocation();
            Log.e("location", "on create");
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            Log.e("location", "play services 1");
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                Log.e("location", "play services 2");
            } else {
                Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
            }
            return false; }
        return true; }
    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        Log.e("location", "build client");

    }
    @Override
    public void onStart(Intent intent, int startId) { super.onStart(intent, startId);
        if (apiClient != null){
            apiClient.connect();

            Log.e("location", "on start");
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        displayLocation();
        return super.onStartCommand(intent, flags, startId); }
    @Override
    public void onConnected(Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) { apiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "connection failed" + connectionResult.getErrorCode());
    }
    void displayLocation(){
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Log.e("location", currLocation+" and "+ lastLocation);
        if (lastLocation != null){
            currLocation.setLatitude(latIntrepid);
            currLocation.setLongitude(longIntrepid);
            locationDifference = lastLocation.distanceTo(currLocation);
            Log.e("location", currLocation.getLatitude()+" and "+ currLocation.getLongitude());
            Log.e("location", lastLocation.getLatitude()+" and "+ lastLocation.getLongitude());
            if (locationDifference < DISPLACEMENT){
                currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                Log.e("location", "im within radius");

            }
        } else {
            Log.e("location", "in else of display");
        }
    } }
