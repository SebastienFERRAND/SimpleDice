package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonHTTP {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JsonHTTP() {

    }

    public JSONObject getJSONFromUrl(String urlString, HashMap<String, String> headers, String method) {

        // Making HTTP request
        try {

            URL url = new URL(urlString);
            HttpURLConnection httpconn  = (HttpURLConnection)url.openConnection();
            httpconn.setRequestMethod(method);

            //while(keySetIterator.hasNext()){ Integer key = keySetIterator.next(); System.out.println("key: " + key + " value: " + map.get(key)); }

            Iterator<String> keySetIterator = headers.keySet().iterator();

            while (keySetIterator.hasNext()){
                String key = keySetIterator.next();
                httpconn.setRequestProperty(key, headers.get(key));
            }
            httpconn.setRequestMethod(method);
            httpconn.setRequestMethod(method);

            BufferedReader input;

            // Faire un switch case
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
            }else{
                input = new BufferedReader(new InputStreamReader(httpconn.getErrorStream()), 8192);
            }

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = input.readLine()) != null) {
                sb.append(line + "n");
                Log.d("Test", line);
            }
            json = sb.toString();
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