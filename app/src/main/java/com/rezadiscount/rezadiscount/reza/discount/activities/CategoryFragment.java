package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.adapter.CategoryAdapter;
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

public class CategoryFragment extends Fragment implements GetLocationListener, GetJsonListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<HashMap<String, String>> shopList;

    private LocationUtility loc;

    private GetJsonResult jsonResult;

    private String latitude;
    private String longitude;

    private JSONArray jsonReturn;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shopList = new ArrayList<>();

        loc = new LocationUtility();
        loc.initialise(getActivity());
        loc.addListener(this);

        View rootView = inflater.inflate(R.layout.find_fragment, container, false);

        //Create the list of Deals
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void getJsonObject() {

        Log.d("JSON Find business", "get return " + jsonResult.getJson().toString());

        try {

            jsonReturn = jsonResult.getJson().getJSONArray(QuickstartPreferences.TAG_RESULT);

            for (int i = 0; i < jsonReturn.length(); i++) {
                JSONObject c = jsonReturn.getJSONObject(i);

                // Storing  JSON item in a Variable
                String idS = c.getString(QuickstartPreferences.TAG_ID);
                String labelS = c.getString(QuickstartPreferences.TAG_NAME);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<>();

                map.put(QuickstartPreferences.TAG_ID, idS);
                map.put(QuickstartPreferences.TAG_NAME, labelS);

                shopList.add(map);


                /*mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent myIntent = new Intent(getActivity(), BusinessResults.class);

                        myIntent.putExtra(QuickstartPreferences.TAG_ID, shopList.get(position).get(QuickstartPreferences.TAG_ID));

                        getActivity().startActivity(myIntent);
                    }
                });*/

            }

            mAdapter = new CategoryAdapter(shopList);
            mRecyclerView.setAdapter(mAdapter);


        } catch (Exception e) {

            Log.d("Error json return", e.getMessage());
            try {
                String status = jsonResult.getJson().getString(QuickstartPreferences.TAG_STATUS);
                String details = jsonResult.getJson().getString(QuickstartPreferences.TAG_DETAIL);

                Toast.makeText(getActivity(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();

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
                    Log.d("GPS", "Permission granted");
                    loc.connectMapAPI();
                } else {
                    Log.d("GPS", "Permission not granted");
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

        Log.d("Long", latitude);
        Log.d("Lat", longitude);

        HashMap<String, String> headerList = new HashMap<>();
        headerList.put(QuickstartPreferences.TAG_LATITUDE, latitude);
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, longitude);
        SharedPreferencesModule.initialise(getActivity());
        headerList.put(QuickstartPreferences.TAG_TOKEN, SharedPreferencesModule.getToken());

        jsonResult = new GetJsonResult();
        jsonResult.setParams(getActivity(), headerList, QuickstartPreferences.URL_CAT, QuickstartPreferences.TAG_GET, null);
        jsonResult.addListener(this);
        jsonResult.execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!loc.checkLocationPermission()) {
            Log.d("GPS", "You have the Permission");
            this.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("GPS", "You don't have the Permission");
            loc.connectMapAPI();
        }
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        loc.disconnectMapAPI();
        super.onStop();
    }
}
