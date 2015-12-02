package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetLocationListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonHTTP;
import com.rezadiscount.rezadiscount.reza.discount.utilities.LocationUtility;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessResults extends BaseDrawerActivity implements GetLocationListener, GetJsonListener {

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    //private static String url = "http://lucas.touratier.fr/api.php";
    private static String url_merc = "merchant";

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

    private static final String TAG_STATUS = "status";
    private static final String TAG_DETAIL = "detail";

    private JSONArray android = null;

    private String latitude;
    private String longitude;

    private LocationUtility loc;

    private GetJsonResult jsonResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Menu Navigation
        setContentView(R.layout.activity_business_results);
        loc = new LocationUtility();
        loc.initialise(this);
        loc.addListener(this);

    }

    @Override
    public void getJsonObject() {
        try {

            android = jsonResult.getJson().getJSONArray(TAG_RESULT);
            for (int i = 0; i < android.length(); i++) {
                JSONObject c = android.getJSONObject(i);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ID, c.getString(TAG_ID));
                map.put(TAG_LABEL, c.getString(TAG_LABEL));
                map.put(TAG_latitude, c.getString(TAG_latitude));
                map.put(TAG_LONGITUDE, c.getString(TAG_LONGITUDE));
                map.put(TAG_DISTANCE, c.getString(TAG_DISTANCE));
                map.put(TAG_PICTURE, c.getString(TAG_PICTURE));
                map.put(TAG_ADRESS, c.getString(TAG_ADRESS));

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
            try {
                String status = jsonResult.getJson().getString(TAG_STATUS);
                String details = jsonResult.getJson().getString(TAG_DETAIL);
                Toast.makeText(getApplicationContext(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();
            } catch (JSONException e1) {
                e1.printStackTrace();
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
                    loc.connectMapAPI();
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
    public void getLaglong() {

        String[] latlong = loc.getLocation();
        latitude = latlong[0];
        longitude = latlong[1];

        HashMap<String, String> headerList = new HashMap<String, String>();

        headerList.put("Accept", "application/json");
        headerList.put("Content-Type", "application/json");
        headerList.put("lat", latitude + "");
        headerList.put("long", longitude + "");
        SharedPreferencesModule.initialise(getApplicationContext());
        headerList.put("token", SharedPreferencesModule.getToken());
        headerList.put("deviceid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));


        jsonResult = new GetJsonResult();
        jsonResult.setParams(this, headerList, url_merc, "GET");
        jsonResult.addListener(this);
        jsonResult.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!loc.checkLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            loc.connectMapAPI();
        }
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        loc.disconnectMapAPI();
        super.onStop();
    }
}
