package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.rezadiscount.rezadiscount.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Sébastien on 11/18/2015.
 */
public class JsonParserLogin extends AsyncTask<String, String, JSONObject> {
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
            if (json.getJSONObject(TAG_RESULT) != null) {

                android = json.getJSONObject(TAG_RESULT);


                JSONObject c = android.getJSONObject(TAG_TOKEN);

                // Storing  JSON item in a Variable
                String token = c.getString(TAG_TOKEN);

                Log.d("Test", "Token : " + token);

            } else {
                Log.d("Test", " BUG test ");
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}