package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;

public class BusinessProfile extends FragmentActivity {


    private TextView id;
    private TextView label;
    private TextView longitude;
    private TextView lattitude;
    private TextView distance;
    private TextView picture;
    private TextView adress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        id = (TextView) findViewById(R.id.id);
        label = (TextView) findViewById(R.id.label);
        longitude = (TextView) findViewById(R.id.longitude);
        lattitude = (TextView) findViewById(R.id.lattitude);
        distance = (TextView) findViewById(R.id.distance);
        picture = (TextView) findViewById(R.id.picture);
        adress = (TextView) findViewById(R.id.adress);


        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        id.setText(myIntent.getStringExtra("id"));
        label.setText(myIntent.getStringExtra("label"));
        longitude.setText(myIntent.getStringExtra("longitude"));
        lattitude.setText(myIntent.getStringExtra("lattitude"));
        distance.setText(myIntent.getStringExtra("distance"));
        picture.setText(myIntent.getStringExtra("picture"));
        adress.setText(myIntent.getStringExtra("adress"));


        Log.d("Test", myIntent.getStringExtra("id").toString());
        Log.d("Test", myIntent.getStringExtra("label").toString());


    }
}
