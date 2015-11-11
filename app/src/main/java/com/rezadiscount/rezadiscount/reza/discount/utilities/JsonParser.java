package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JsonParser() {

    }

    public JSONObject getJSONFromUrl(String urlString) {

        // Making HTTP request
        try {

            URL url = new URL(urlString);
            HttpURLConnection httpconn  = (HttpURLConnection)url.openConnection();
            httpconn.setRequestMethod("GET");
            httpconn.setRequestProperty("Accept", "application/json");
            httpconn.setRequestProperty("Content-Type", "application/json");
            //httpconn.setRequestProperty("lat", "3.0");
            //httpconn.setRequestProperty("long", "2.0");
            Log.d("Test", httpconn.getResponseCode() + " test ");

            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);

            Log.d("Test", "1");
            StringBuilder sb = new StringBuilder();
            Log.d("Test", "2");
            String line = null;
            Log.d("Test", "3");
            while ((line = input.readLine()) != null) {
                Log.d("Test", line + " Ligne ");
                sb.append(line + "n");
            }
            json = sb.toString();

            Log.d("Test", sb.toString() + " String ");

            // Faire un switch case
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("Test", sb.toString() + " String ");
            }else{
                Log.d("Test", sb.toString() + " String ");
            }
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}