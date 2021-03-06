package com.appology.grenadeal.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appology.grenadeal.HTTPObjects.CategoryReturn;
import com.appology.grenadeal.R;
import com.appology.grenadeal.WebServices.GetJsonListenerCategory;
import com.appology.grenadeal.WebServices.GetJsonResultCategory;
import com.appology.grenadeal.adapter.CategoryAdapter;
import com.appology.grenadeal.utilities.GetLocationListener;
import com.appology.grenadeal.utilities.LocationUtility;
import com.appology.grenadeal.utilities.QuickstartPreferences;
import com.appology.grenadeal.utilities.SharedPreferencesModule;

import java.util.HashMap;

public class FragmentCategory extends Fragment implements GetLocationListener, GetJsonListenerCategory {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LocationUtility loc;

    private GetJsonResultCategory jsonResultCategory;
    private GetJsonListenerCategory jsonListenerCategory;

    public FragmentCategory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loc = new LocationUtility();
        loc.initialise(getActivity());
        loc.addListener(this);

        jsonListenerCategory = this;

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

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
    public void getReturnCategory() {
        CategoryReturn categoryReturn = jsonResultCategory.getReturnCategories();

//        if (categoryReturn.getHTTPCode().equals(QuickstartPreferences.TAG_HTTP_SUCCESS)) {

        Log.d("List category", categoryReturn.getListCategories().get(0).getId() + "");
        Log.d("List category", categoryReturn.getListCategories().get(0).getName());

        mAdapter = new CategoryAdapter(categoryReturn.getListCategories(), getActivity());
        mRecyclerView.setAdapter(mAdapter);

//        } else { //TODO deal with custom error messages
//            Toast.makeText(getActivity(), "Can't get categories", Toast.LENGTH_LONG).show();
//        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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

    // When GPS was able to locate, then we get categories
    @Override
    public void getLaglong() {

        String[] latlong = loc.getLocation();
        getCategories(latlong[0], latlong[1]);

    }

    private void getCategories(String latitude, String longitude) {


        HashMap<String, String> headerList = new HashMap<>();
        headerList.put(QuickstartPreferences.TAG_LATITUDE, latitude);
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, longitude);
        SharedPreferencesModule.initialise(getActivity());
        headerList.put(QuickstartPreferences.TAG_TOKEN, SharedPreferencesModule.getToken());

        jsonResultCategory = new GetJsonResultCategory();
        jsonResultCategory.setParams(getActivity(), headerList, null);
        jsonResultCategory.addListener(jsonListenerCategory);
        jsonResultCategory.execute();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        loc.disconnectMapAPI();
        super.onStop();
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
            // If there's no item on the list then feed it
            if (mAdapter == null) {
                loc.connectMapAPI();
            }
        }
    }
}
