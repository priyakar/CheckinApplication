package com.example.priyakarambelkar.checkinapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.Timer;

import static com.example.priyakarambelkar.checkinapplication.R.drawable.slackicon;

public class CheckLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient apiClient;
    public static boolean hasCheckedIn = false;
    private static int UPDATE_INTERVAL = 1000*60*15;
    private PendingIntent yesReceive;
    private PendingIntent noReceive;
    public static NotificationManager notificationManager;

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
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
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
        timer.schedule(interval, UPDATE_INTERVAL, 1000 * 15 * 60);
        if (CheckLocationAfterIntervals.chechIn && !hasCheckedIn) {
            timer.cancel();
            Log.e("check this out", "after timer cancel");
            stopSelf();
            notifyReceiver();
            Notification notification = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.notify)
                    .setContentTitle("Post your location on slack")
                    .setContentText("Would you like to post a notification on #whos-here?")
                    .addAction(R.drawable.yes, "YES", yesReceive)
                    .addAction(R.drawable.no, "NO", noReceive).build();
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }
    private void notifyReceiver() {
        Intent postMsg = new Intent(getApplicationContext(), NotificationReceiver.class);
        postMsg.setAction("YES");
        yesReceive = PendingIntent.getBroadcast(this, 123, postMsg, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent noPostMsg = new Intent(getApplicationContext(), NotificationReceiver.class);
        noPostMsg.setAction("NO");
        noReceive = PendingIntent.getBroadcast(this, 123, noPostMsg, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
