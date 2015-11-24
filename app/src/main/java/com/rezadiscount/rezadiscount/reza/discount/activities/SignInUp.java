package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rezadiscount.rezadiscount.R;


import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonHTTP;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInUp extends AppCompatActivity {

    private Button connexion;
    private Button inscription;

    private EditText connexionField;
    private EditText passwordField;

    private static String url_merc = "auth";
    private static final String TAG_RESULT = "results";
    private static final String TAG_TOKEN = "token";
    private JSONObject android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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

                new JsonParserLogin().execute(connexionField.getText().toString(), passwordField.getText().toString(), url_merc);

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

    }


    private class JsonParserLogin extends AsyncTask<String, String, JSONObject>

    {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInUp.this);
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
            headerList.put("lat", "3.0");
            headerList.put("long", "2.0");
            headerList.put("login", args[0]);
            headerList.put("password", args[1]);
            headerList.put("deviceid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));

            Log.d("Test", "Device ID : " + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            Log.d("Test", getResources().getString(R.string.url_api) + url_merc);

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url_api) + url_merc, headerList, "GET");

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                if (json != null) {

                    android = json.getJSONObject(TAG_RESULT);

                    // Storing  JSON item in a Variable
                    String token = android.getString(TAG_TOKEN);

                    SharedPreferencesModule.initialise(getApplicationContext());
                    SharedPreferencesModule.setToken(token);

                    Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SignInUp.this.startActivity(myIntent);

                } else {
                    Log.d("Test", " BUG test ");
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }


    }


}
