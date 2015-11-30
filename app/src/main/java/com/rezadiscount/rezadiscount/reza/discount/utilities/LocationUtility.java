package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rezadiscount.rezadiscount.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastienferrand on 11/29/15.
 */
public class LocationUtility implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private String[] latlong;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private boolean isUrlRequested = false;

    private double latitude;
    private double longitude;

    private Activity activity;

    private List<GetLocationListener> listeners = new ArrayList<GetLocationListener>();


    public void initialise(Activity act) {

        activity = act;

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connectMapAPI() {
        if (!checkGPSEnabled()) {
            showMessageActivateGPS();
        } else {
            mGoogleApiClient.connect();
        }
    }

    public void disconnectMapAPI() {
        mGoogleApiClient.disconnect();
    }

    public String[] getLocation() {
        String[] latlong = new String[2];
        latlong[0] = latitude + "";
        latlong[1] = longitude + "";

        return latlong;
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (!isUrlRequested) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Testu", latitude + " U");
            Log.d("Testu", longitude + " U");
            isUrlRequested = true;
            for (GetLocationListener hl : listeners)
                hl.getLaglong();
        }
    }

    public boolean checkGPSEnabled() {

        LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(callGPSSettingIntent);

                    mGoogleApiClient.connect();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public void showMessageActivateGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String activateGPS = activity.getResources().getString(R.string.activate_gps);
        builder.setMessage(activateGPS).setPositiveButton(activity.getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();
    }

    public void addListener(GetLocationListener toAdd) {
        listeners.add(toAdd);
    }


    /////// PERMISSION //////////
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}
