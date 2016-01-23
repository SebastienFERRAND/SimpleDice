package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/* DEPRECATED */
public class JsonHTTP {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JsonHTTP() {

    }

    public JSONObject getJSONFromUrl(String urlString, HashMap<String, String> headers, String method, JSONObject jsonBody) {

        // Making HTTP request
        try {

            URL url = new URL(urlString);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setRequestMethod(method);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpconn.setRequestProperty(entry.getKey(), entry.getValue());

                Log.d("Header", entry.getKey() + " " + entry.getValue());
            }

            if (jsonBody != null) {
                OutputStream os = httpconn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.close();

                Log.d("Body", "Body : " + jsonBody.toString());
            }

            BufferedReader input;

            // Faire un switch case
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
            } else {
                input = new BufferedReader(new InputStreamReader(httpconn.getErrorStream()), 8192);
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line + "n");
                Log.d("Result HTTP", line);
            }
            json = sb.toString();


        } catch (Exception e) {
            Log.e("Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("Error", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
}