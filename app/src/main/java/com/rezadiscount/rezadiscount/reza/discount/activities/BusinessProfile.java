package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;

public class BusinessProfile extends BaseDrawerActivity {


    private TextView id;
    private TextView label;
    private TextView longitude;
    private TextView latitude;
    private TextView distance;
    private TextView picture;
    private TextView adress;
    private Button seeMap;

    public Double longitudeS;
    public Double latitudeS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        id = (TextView) findViewById(R.id.id);
        label = (TextView) findViewById(R.id.label);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        distance = (TextView) findViewById(R.id.distance);
        picture = (TextView) findViewById(R.id.picture);
        adress = (TextView) findViewById(R.id.adress);

        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent

        longitudeS = Double.parseDouble(myIntent.getStringExtra("longitude"));
        latitudeS = Double.parseDouble(myIntent.getStringExtra("latitude"));

        id.setText(myIntent.getStringExtra("id"));
        label.setText(myIntent.getStringExtra("label"));
        longitude.setText(myIntent.getStringExtra("longitude"));
        latitude.setText(myIntent.getStringExtra("latitude"));
        distance.setText(myIntent.getStringExtra("distance"));
        picture.setText(myIntent.getStringExtra("picture"));
        adress.setText(myIntent.getStringExtra("adress"));

        seeMap = (Button) findViewById(R.id.seeMap);

        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToMap = new Intent(BusinessProfile.this, BusinessLocation.class);

                intentToMap.putExtra("latitude", latitudeS);
                intentToMap.putExtra("longitude", longitudeS);

                BusinessProfile.this.startActivity(intentToMap);
            }
        });


    }
}
