package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rezadiscount.rezadiscount.R;

import com.rezadiscount.rezadiscount.reza.discount.services.RegistrationIntentService;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInUp extends AppCompatActivity implements GetJsonListener{

    private Button connexion;
    private Button inscription;

    private EditText connexionField;
    private EditText passwordField;
    private JSONObject androidV = null;

    private Context context;

    private GetJsonListener jsonListener;

    private GetJsonResult jsonResult;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        jsonListener = this;

        setContentView(R.layout.activity_sign_in_up);

        connexion = (Button) findViewById(R.id.connexion);
        inscription = (Button) findViewById(R.id.inscription);

        connexionField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.passwd_field);


        //if a token already exist, no need to sign in
        SharedPreferencesModule.initialise(getApplicationContext());
        if (!SharedPreferencesModule.getToken().equals("")){
            Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SignInUp.this.startActivity(myIntent);
        }

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting JSON from URL
                HashMap<String, String> headerList = new HashMap<String, String>();
                headerList.put(QuickstartPreferences.TAG_LOGIN, connexionField.getText().toString());
                headerList.put(QuickstartPreferences.TAG_PASSWD, passwordField.getText().toString());
                headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
                SharedPreferencesModule.initialise(getApplicationContext());
                headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
                headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

                jsonResult = new GetJsonResult();
                jsonResult.setParams(context, headerList, QuickstartPreferences.url_auth, "GET");
                jsonResult.addListener(jsonListener);
                jsonResult.execute();

            }
        });

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInUp.this, SubscribeActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SignInUp.this.startActivity(myIntent);
            }
        });

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            //Intent intent = new Intent(this, RegistrationIntentService.class);
            //startService(intent);
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    public void getJsonObject() {
        try {
            androidV = jsonResult.getJson().getJSONObject(QuickstartPreferences.TAG_RESULT);

            // Storing  JSON item in a Variable
            String token = androidV.getString(QuickstartPreferences.TAG_TOKEN);

            SharedPreferencesModule.initialise(getApplicationContext());
            SharedPreferencesModule.setToken(token);

            Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SignInUp.this.startActivity(myIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Test", "This device is not supported.");
                finish();
            }
            Log.i("Test", "This device is not supported.");
            return false;
        }
        Log.i("Test", "This device is supported.");
        return true;
    }


}
