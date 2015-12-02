package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;

import android.support.v4.app.FragmentActivity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;

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


        id =myIntent.getStringExtra("id");
        label = myIntent.getStringExtra("label");
        distance = myIntent.getStringExtra("distance");
        picture = myIntent.getStringExtra("picture");
        adress = myIntent.getStringExtra("adress");
        latitudeS = Double.parseDouble(myIntent.getStringExtra("latitude"));
        longitudeS = Double.parseDouble(myIntent.getStringExtra("longitude"));
        Log.d("map", latitudeS + " activity lat");
        Log.d("map", longitudeS + "activity long ");



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
