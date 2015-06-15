package com.example.priyakarambelkar.checkinapplication;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyakarambelkar.checkinapplication.CheckLocationService;
import com.example.priyakarambelkar.checkinapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_REQUEST = 1000; private Location lastLocation, currLocation;
    private GoogleApiClient apiClient;
    private boolean requestUpdates = false;
    private LocationRequest locationRequest; private static int UPDATE_INTERVAL = 300000;
    private static int DISPLACEMENT = 50;
    double latIntrepid = 42.367266;
    double longIntrepid = -71.080130;
    float locationDifference =0;
    LocationManager locationManager;
    @InjectView(R.id.display_location)
    TextView displayLocation;
    @InjectView(R.id.btn_fetch)
    Button btnFetch;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); ButterKnife.inject(this);
        if (checkPlayServices()){
            buildGoogleApiClient();
            Log.e("location", "on create");
        }
        // intent call to start service
    }
    @OnClick(R.id.btn_fetch) void onClick (){
       // startService(new Intent(this, CheckLocationService.class));
        displayLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action barwil
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { return true;
        }
        return super.onOptionsItemSelected(item); }
    @Override
    public void onConnected(Bundle bundle) { //displayLocation();
    }
    @Override
    public void onConnectionSuspended(int i) { apiClient.connect();
    }
    // automatically handle clicks on the Home/Up button, so // as you specify a parent activity in


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "connection failed" + connectionResult.getErrorCode());
    }
     @Override
     protected void onStart() {
         super.onStart();
         if (apiClient != null){
             apiClient.connect();
         }
 }
    private boolean checkPlayServices() {
        int resultCode =
GooglePlayServicesUtil .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if
(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
GooglePlayServicesUtil.getErrorDialog(resultCode, this,
PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                finish();
}
            return false;
        }
        return true;
    }
    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    private void getSpinningLocation(){
        if (locationManager == null){
            locationManager = (LocationManager)
getSystemService(LOCATION_SERVICE);
            if
(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
){
                lastLocation =
locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, DISPLACEMENT, GPSListener);
} }
}
    private LocationListener GPSListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) { locationManager.removeUpdates(GPSListener);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) { }
    };
    void displayLocation(){
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        currLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (lastLocation != null){
            currLocation.setLatitude(latIntrepid);
            currLocation.setLongitude(longIntrepid);
            locationDifference = lastLocation.distanceTo(currLocation);
            Log.e("location", ""+locationDifference);
            if (locationDifference < DISPLACEMENT){
                Log.e("location", "im within radius");

            }
        } else {
            Log.e("location", "in else of display");
        }
    }
}
