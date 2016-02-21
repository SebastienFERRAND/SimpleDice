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
 * This class is to send a request in POST or get Asynchronously to the server
 * By passing header and body
 */

public class GetJsonResultSignUp extends AsyncTask<String, String, JSONObject>

{
    private ProgressDialog pDialog;
    private Context context;
    private HashMap<String, String> listHeaders;
    private String resultJSON;
    private JSONObject json;
    private JSONObject bodyJson;

    private HTTPStandardReturn signUpReturn;

    private List<GetJsonListenerSignUp> listeners = new ArrayList<>();

    public void setParams(Context con, HashMap<String, String> listHeadersP, JSONObject body) {
        context = con;
        listHeaders = listHeadersP;
        listHeaders.put("Accept", "application/json");
        listHeaders.put("Content-Type", "application/json");
        listHeaders.put("deviceid", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        bodyJson = body;

        for (Map.Entry<String, String> entry : listHeadersP.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Log.d("Header", key + " " + value);

            // do what you have to do here
            // In your case, an other loop.
        }

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
        json = this.getJSONFromUrl(listHeaders, bodyJson);

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonP) {
        pDialog.dismiss();


        // if Json return isn't null
        if (json != null) {

            // If subscription
            try {
                signUpReturn = new HTTPStandardReturn();
                signUpReturn.setCode(json.getString(QuickstartPreferences.TAG_HTTPCODE));
                signUpReturn.setMessage(json.getString(QuickstartPreferences.TAG_MESSAGE));
                signUpReturn.setSource(json.getString(QuickstartPreferences.TAG_SOURCE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            Log.d("JSON", "get return " + jsonP.toString());
            // Getting JSON Array from URL
            json = jsonP;
            for (GetJsonListenerSignUp hl : listeners) {
                hl.getReturnSignUp();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public JSONObject getJSONFromUrl(HashMap<String, String> headers, JSONObject jsonBody) {

        // Making HTTP request
        try {

            URL url = new URL(QuickstartPreferences.URL_SERV + QuickstartPreferences.URL_REG);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setRequestMethod(QuickstartPreferences.TAG_POST);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpconn.setRequestProperty(entry.getKey(), entry.getValue());
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

    public void addListener(GetJsonListenerSignUp toAdd) {
        listeners.add(toAdd);
    }


    // Method to call when Post execute is done
    public HTTPStandardReturn getReturnSignUp() {
        return signUpReturn;
    }

}