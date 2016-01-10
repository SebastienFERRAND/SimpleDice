package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetLocationListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.LocationUtility;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class BusinessFilterResearch extends BaseDrawerActivity implements GetLocationListener, GetJsonListener {

    private Button research;

    private String latitude;
    private String longitude;

    private JSONArray androidV = null;

    private ListView listCategories;

    private ArrayList<HashMap<String, String>> oslist = new ArrayList<>();

    private LocationUtility loc;


    private GetJsonResult jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_filter_research);

        loc = new LocationUtility();
        loc.initialise(this);
        loc.addListener(this);

    }

    @Override
    public void getJsonObject() {

        Log.d("Test1", "get return " + jsonResult.getJson().toString());

        try {

            androidV = jsonResult.getJson().getJSONArray(QuickstartPreferences.TAG_RESULT);

            for (int i = 0; i < androidV.length(); i++) {
                JSONObject c = androidV.getJSONObject(i);

                // Storing  JSON item in a Variable
                String idS = c.getString(QuickstartPreferences.TAG_ID);
                String labelS = c.getString(QuickstartPreferences.TAG_NAME);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<>();

                map.put(QuickstartPreferences.TAG_ID, idS);
                map.put(QuickstartPreferences.TAG_NAME, labelS);

                oslist.add(map);
                listCategories = (ListView) findViewById(R.id.list_categories);

                ListAdapter adapter = new SimpleAdapter(BusinessFilterResearch.this, oslist,
                        R.layout.category_row_item,
                        new String[]{QuickstartPreferences.TAG_ID, QuickstartPreferences.TAG_NAME}, new int[]{
                        R.id.id, R.id.label});

                listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent myIntent = new Intent(BusinessFilterResearch.this, BusinessResults.class);

                        myIntent.putExtra(QuickstartPreferences.TAG_ID, oslist.get(position).get(QuickstartPreferences.TAG_ID));

                        BusinessFilterResearch.this.startActivity(myIntent);
                    }
                });

                listCategories.setAdapter(adapter);

            }


        } catch (Exception e) {

            Log.d("Test", e.getMessage());
            try {
                String status = jsonResult.getJson().getString(QuickstartPreferences.TAG_STATUS);
                String details = jsonResult.getJson().getString(QuickstartPreferences.TAG_DETAIL);

                Toast.makeText(getApplicationContext(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();

            } catch (Exception e1) {
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
                    Log.e("Permission error", "Missing permission authorization");
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

        HashMap<String, String> headerList = new HashMap<>();
        headerList.put(QuickstartPreferences.TAG_LATITUDE, latitude);
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, longitude);
        SharedPreferencesModule.initialise(this);
        headerList.put(QuickstartPreferences.TAG_TOKEN, SharedPreferencesModule.getToken());

        jsonResult = new GetJsonResult();
        jsonResult.setParams(this, headerList, QuickstartPreferences.URL_CAT, QuickstartPreferences.TAG_GET, null);
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