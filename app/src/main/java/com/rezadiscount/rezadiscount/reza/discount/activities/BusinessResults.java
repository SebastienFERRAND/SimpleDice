package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerMerchant;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultMerchant;
import com.rezadiscount.rezadiscount.reza.discount.components.ActivityBaseDrawer;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetLocationListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.LocationUtility;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessResults extends ActivityBaseDrawer implements GetLocationListener, GetJsonListenerMerchant {

    ArrayList<HashMap<String, String>> oslist = new ArrayList<>();

    private ListView list;
    private JSONArray android = null;

    private String latitude;
    private String longitude;

    private LocationUtility loc;

    private GetJsonResultMerchant jsonResult;


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
    public void getReturnMerchant() {
        try {

            android = jsonResult.getJson().getJSONArray(QuickstartPreferences.TAG_RESULT);
            for (int i = 0; i < android.length(); i++) {
                JSONObject c = android.getJSONObject(i);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<>();

                map.put(QuickstartPreferences.TAG_ID, c.getString(QuickstartPreferences.TAG_ID));
                map.put(QuickstartPreferences.TAG_NAME, c.getString(QuickstartPreferences.TAG_NAME));
                map.put(QuickstartPreferences.TAG_LATITUDE, c.getString(QuickstartPreferences.TAG_LATITUDE));
                map.put(QuickstartPreferences.TAG_LONGITUDE, c.getString(QuickstartPreferences.TAG_LONGITUDE));
                map.put(QuickstartPreferences.TAG_DISTANCE, c.getString(QuickstartPreferences.TAG_DISTANCE));
                map.put(QuickstartPreferences.TAG_PICTURE, c.getString(QuickstartPreferences.TAG_PICTURE));
                map.put(QuickstartPreferences.TAG_ADRESS, c.getString(QuickstartPreferences.TAG_ADRESS));

                oslist.add(map);
                list = (ListView) findViewById(R.id.list);

                ListAdapter adapter = new SimpleAdapter(BusinessResults.this, oslist,
                        R.layout.business_row_item,
                        new String[]{QuickstartPreferences.TAG_ID, QuickstartPreferences.TAG_NAME, QuickstartPreferences.TAG_DISTANCE}, new int[]{
                        R.id.id, R.id.label, R.id.distance});

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent myIntent = new Intent(BusinessResults.this, BusinessProfile.class);

                        myIntent.putExtra(QuickstartPreferences.TAG_ID, oslist.get(position).get(QuickstartPreferences.TAG_ID));
                        myIntent.putExtra(QuickstartPreferences.TAG_NAME, oslist.get(position).get(QuickstartPreferences.TAG_NAME));
                        myIntent.putExtra(QuickstartPreferences.TAG_LATITUDE, oslist.get(position).get(QuickstartPreferences.TAG_LATITUDE));
                        myIntent.putExtra(QuickstartPreferences.TAG_LONGITUDE, oslist.get(position).get(QuickstartPreferences.TAG_LONGITUDE));
                        myIntent.putExtra(QuickstartPreferences.TAG_DISTANCE, oslist.get(position).get(QuickstartPreferences.TAG_DISTANCE));
                        myIntent.putExtra(QuickstartPreferences.TAG_PICTURE, oslist.get(position).get(QuickstartPreferences.TAG_PICTURE));
                        myIntent.putExtra(QuickstartPreferences.TAG_ADRESS, oslist.get(position).get(QuickstartPreferences.TAG_ADRESS));

                        BusinessResults.this.startActivity(myIntent);
                    }
                });
            }
        } catch (Exception e) {
            try {
                Log.d("debug", e.getMessage());
                String status = jsonResult.getJson().getString(QuickstartPreferences.TAG_STATUS);
                String details = jsonResult.getJson().getString(QuickstartPreferences.TAG_DETAIL);
                Toast.makeText(getApplicationContext(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loc.connectMapAPI();
                } else {
                    Toast.makeText(this, "Location permission was denied", Toast.LENGTH_LONG);
                }
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

        Intent myIntent = getIntent();

        HashMap<String, String> headerList = new HashMap<>();
        headerList.put(QuickstartPreferences.TAG_LATITUDE, latitude + "");
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, longitude + "");
        headerList.put(QuickstartPreferences.TAG_CATEGORIES, myIntent.getStringExtra(QuickstartPreferences.TAG_ID));
        SharedPreferencesModule.initialise(this);
        headerList.put(QuickstartPreferences.TAG_TOKEN, SharedPreferencesModule.getToken());

        jsonResult = new GetJsonResultMerchant();
        jsonResult.setParams(this, headerList, QuickstartPreferences.URL_MERC, QuickstartPreferences.TAG_GET, null);
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
