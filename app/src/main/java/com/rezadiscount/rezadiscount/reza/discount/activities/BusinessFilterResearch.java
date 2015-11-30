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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;

import com.rezadiscount.rezadiscount.reza.discount.utilities.GetLocationListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonHTTP;
import com.rezadiscount.rezadiscount.reza.discount.utilities.LocationUtility;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessFilterResearch extends BaseDrawerActivity implements GetLocationListener {

    private Button research;

    private String latitude;
    private String longitude;

    private JSONArray android = null;

    private ListView listCategories;


    private static final String TAG_RESULT = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_LABEL = "name";

    private static String url = "category";

    private static final String TAG_STATUS = "status";
    private static final String TAG_DETAIL = "detail";

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    LocationUtility loc = new LocationUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_filter_research);

        research = (Button) findViewById(R.id.research);
        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToMap = new Intent(BusinessFilterResearch.this, BusinessResults.class);
                BusinessFilterResearch.this.startActivity(intentToMap);

            }
        });

        loc.initialise(this);
        loc.addListener(this);

    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessFilterResearch.this);
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
            headerList.put("lat", latitude);
            headerList.put("long", longitude);
            SharedPreferencesModule.initialise(getApplicationContext());
            headerList.put("token", SharedPreferencesModule.getToken());
            headerList.put("deviceid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));

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

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_ID, idS);
                    map.put(TAG_LABEL, labelS);

                    oslist.add(map);
                    listCategories = (ListView) findViewById(R.id.list_categories);

                    ListAdapter adapter = new SimpleAdapter(BusinessFilterResearch.this, oslist,
                            R.layout.category_row_item,
                            new String[]{TAG_ID, TAG_LABEL}, new int[]{
                            R.id.id, R.id.label});

                    listCategories.setAdapter(adapter);

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
        Log.d("token", "latitude latitude : " + longitude);
        longitude = latlong[1];
        Log.d("token", "longitude longitude : " + longitude);

        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
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