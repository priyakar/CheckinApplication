package com.example.priyakarambelkar.checkinapplication;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient apiClient;

    @InjectView(R.id.btn_fetch)
    Button btnFetch;

    //Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); ButterKnife.inject(this);
       // intent = new Intent(this, CheckLocationService.class);

    }
    @OnClick(R.id.btn_fetch) void onClick (){

         startService(new Intent(this, CheckLocationService.class));
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
    public void onConnectionSuspended(int i) {
        apiClient.connect();
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

}
