package com.rezadiscount.rezadiscount.reza.discount.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.HTTPStandardReturn;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sebastienferrand on 12/2/15.
 * This class is to send a request in POST Asynchronously to the server
 * By passing header and body
 * This class is for password recover
 */

public class GetJsonResultPassword extends AsyncTask<String, String, JSONObject>

{
    private ProgressDialog pDialog;
    private Context context;
    private String resultJSON;
    private JSONObject json;
    private JSONObject bodyJson;
    private HTTPStandardReturn passwordReturn;

    private List<GetJsonListenerPassword> listeners = new ArrayList<>();

    public void setParams(Context con, JSONObject body) {
        Log.d("Password", "Password recovery starting");

        context = con;
        bodyJson = body;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.waiting));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        // Getting JSON from URL
        json = this.getJSONFromUrl(bodyJson);

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonP) {

        pDialog.dismiss();

        // if Json return isn't null
        if (json != null) {

            // If subscription
            try {
                passwordReturn = new HTTPStandardReturn();
                passwordReturn.setHTTPCode(json.getString(QuickstartPreferences.TAG_HTTPCODE));
                passwordReturn.setMessage(json.getString(QuickstartPreferences.TAG_MESSAGE));
                passwordReturn.setSource(json.getString(QuickstartPreferences.TAG_SOURCE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Calling listeners
        try {
            json = jsonP;
            for (GetJsonListenerPassword hl : listeners) {
                hl.getReturnPassword();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /*
    Sending password request to server
     */
    private JSONObject getJSONFromUrl(JSONObject jsonBody) {

        // Making HTTP request
        try {

            URL url = new URL(QuickstartPreferences.URL_SERV + QuickstartPreferences.URL_FORPSSWD);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

            httpconn.setConnectTimeout(15000);
            httpconn.setReadTimeout(15000);

            httpconn.setRequestMethod(QuickstartPreferences.TAG_POST);

            // Setting headers
            HashMap<String, String> listHeaders = new HashMap<>();
            listHeaders.put("Accept", "application/json");
            listHeaders.put("Content-Type", "application/json");
            listHeaders.put("deviceid", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            for (Map.Entry<String, String> entry : listHeaders.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d("Header", key + " " + value);
                httpconn.setRequestProperty(key, value);
            }

            // Setting body

            Log.d("Body", "Body : " + jsonBody.toString());
            OutputStream os = httpconn.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader input;

            // Requete effectuee et verification du code retour
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
            resultJSON = sb.toString();


        } catch (Exception e) {
            Log.e("Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            json = new JSONObject(resultJSON);
        } catch (JSONException e) {
            Log.d("Error", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.d("Error", "Unable to connect to the server. Do you have internet ?");
        }
        // return JSON String
        return json;
    }

    public void addListener(GetJsonListenerPassword toAdd) {
        listeners.add(toAdd);
    }


    // Method to call when Post execute is done
    public HTTPStandardReturn getReturnPassword() {
        return passwordReturn;
    }

}