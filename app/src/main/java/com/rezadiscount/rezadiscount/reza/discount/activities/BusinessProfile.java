package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

public class BusinessProfile extends BaseDrawerActivity  implements
        FragmentProfile.OnFragmentInteractionListener,
        FragmentReviews.OnFragmentInteractionListener,
        FragmentMap.OnFragmentInteractionListener {

    private Double longitudeS;
    private Double latitudeS;
    private String id;
    private String label;
    private String distance;
    private String picture;
    private String adress;

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent


        id = myIntent.getStringExtra(QuickstartPreferences.TAG_ID);
        label = myIntent.getStringExtra(QuickstartPreferences.TAG_LABEL);
        distance = myIntent.getStringExtra(QuickstartPreferences.TAG_DISTANCE);
        picture = myIntent.getStringExtra(QuickstartPreferences.TAG_PICTURE);
        adress = myIntent.getStringExtra(QuickstartPreferences.TAG_ADRESS);
        latitudeS = Double.parseDouble(myIntent.getStringExtra(QuickstartPreferences.TAG_LATITUDE));
        longitudeS = Double.parseDouble(myIntent.getStringExtra(QuickstartPreferences.TAG_LONGITUDE));

        FragmentMap.newInstance(latitudeS, longitudeS);
        FragmentProfile.newInstance(id, label, latitudeS, longitudeS, distance, picture, adress);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Profile").setIndicator("Profile"),
                FragmentProfile.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Reviews").setIndicator("Reviews"),
                FragmentReviews.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Map").setIndicator("Map"),
                FragmentMap.class, null);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
