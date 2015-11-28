package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;
import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonHTTP;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessResults extends BaseDrawerActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    //private static String url = "http://lucas.touratier.fr/api.php";
    private static String url = "merchant";

    private ListView list;

    //JSON Node Names
    private static final String TAG_RESULT = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_LABEL = "name";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_PICTURE = "picture";
    private static final String TAG_ADRESS = "adress";

    //if error HTTP

    private static final String TAG_TYPE = "type";
    private static final String TAG_TITLE = "title";
    private static final String TAG_STATUS = "status";
    private static final String TAG_DETAIL = "detail";


    private JSONArray android = null;
    private LocationRequest mLocationRequest;

    private double latitude;
    private double longitude;

    private GoogleApiClient mGoogleApiClient;

    private boolean isUrlRequested = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Menu Navigation
        setContentView(R.layout.activity_business_results);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);

        //Populate Businesses items

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessResults.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JsonHTTP jParser = new JsonHTTP();

            // Getting JSON from URL
            HashMap<String, String> headerList = new HashMap<String, String>();

            headerList.put("Accept", "application/json");
            headerList.put("Content-Type", "application/json");
            headerList.put("lat", latitude + "");
            headerList.put("long", longitude + "");
            SharedPreferencesModule.initialise(getApplicationContext());
            headerList.put("token", SharedPreferencesModule.getToken());
            headerList.put("deviceid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            headerList.put("tokenG", SharedPreferencesModule.getGCMToken());

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url_api) + url, headerList, "GET");

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                android = json.getJSONArray(TAG_RESULT);
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String idS = c.getString(TAG_ID);
                    String labelS = c.getString(TAG_LABEL);
                    String latitudeS = c.getString(TAG_latitude);
                    String longitudeS = c.getString(TAG_LONGITUDE);
                    String distanceS = c.getString(TAG_DISTANCE);
                    String pictureS = c.getString(TAG_PICTURE);
                    String adressS = c.getString(TAG_ADRESS);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_ID, idS);
                    map.put(TAG_LABEL, labelS);
                    map.put(TAG_latitude, latitudeS);
                    map.put(TAG_LONGITUDE, longitudeS);
                    map.put(TAG_DISTANCE, distanceS);
                    map.put(TAG_PICTURE, pictureS);
                    map.put(TAG_ADRESS, adressS);

                    oslist.add(map);
                    list = (ListView) findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(BusinessResults.this, oslist,
                            R.layout.business_row_item,
                            new String[]{TAG_ID, TAG_LABEL, TAG_DISTANCE}, new int[]{
                            R.id.id, R.id.label, R.id.distance});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent myIntent = new Intent(BusinessResults.this, BusinessProfile.class);

                            myIntent.putExtra("id", oslist.get(position).get("id"));
                            myIntent.putExtra("label", oslist.get(position).get("label"));
                            myIntent.putExtra("latitude", oslist.get(position).get("latitude"));
                            myIntent.putExtra("longitude", oslist.get(position).get("longitude"));
                            myIntent.putExtra("distance", oslist.get(position).get("distance"));
                            myIntent.putExtra("picture", oslist.get(position).get("picture"));
                            myIntent.putExtra("adress", oslist.get(position).get("adress"));

                            BusinessResults.this.startActivity(myIntent);
                        }
                    });
                }


            } catch (Exception e) {

                Log.d("Test", e.getMessage());
                try {
                    String status = json.getString(TAG_STATUS);
                    String details = json.getString(TAG_DETAIL);

                    Toast.makeText(getApplicationContext(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkGPSEnabled()) {
                        mGoogleApiClient.connect();
                    }else{
                        showMessageActivateGPS();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (checkLocationPermission()){
            if (checkGPSEnabled()) {
                mGoogleApiClient.connect();
            }else{
                showMessageActivateGPS();
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
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

        if (!isUrlRequested){
        latitude = location.getLatitude();
        Log.d("token", "latitude latitude : " + longitude);
        longitude = location.getLongitude();
        Log.d("token", "longitude longitude : " + longitude);

            oslist = new ArrayList<HashMap<String, String>>();
            new JSONParse().execute();
            isUrlRequested = true;
        }

    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public boolean checkGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);

                    mGoogleApiClient.connect();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void showMessageActivateGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String activateGPS = getResources().getString(R.string.activate_gps);
        builder.setMessage(activateGPS).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();
    }
}
