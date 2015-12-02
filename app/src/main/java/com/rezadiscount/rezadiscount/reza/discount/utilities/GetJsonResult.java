package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.activities.BusinessFilterResearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sebastienferrand on 12/2/15.
 */

public class GetJsonResult extends AsyncTask<String, String, JSONObject>

{
    private ProgressDialog pDialog;
    private Context context;
    private HashMap<String, String> listHeaders;
    private String url;
    private String method;
    private JSONObject json;

    private List<GetJsonListener> listeners = new ArrayList<GetJsonListener>();

    public void setParams(Context con, HashMap<String, String> listHeadersP, String urlP, String methodP){
        context = con;
        listHeaders = listHeadersP;
        url = urlP;
        method = methodP;

    }

    public JSONObject getJson(){
        return json;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Getting Data ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        JsonHTTP jParser = new JsonHTTP();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(QuickstartPreferences.URL_MERC + url, listHeaders, method);

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonP) {
        pDialog.dismiss();
        Log.d("Test1", "get return " + jsonP.toString());
        // Getting JSON Array from URL
        if (jsonP != null) {

            json = jsonP;
            for (GetJsonListener hl : listeners)
                hl.getJsonObject();
        } else {
            Log.d("Test", " BUG test ");
        }

    }

    public void addListener(GetJsonListener toAdd) {
        listeners.add(toAdd);
    }
}