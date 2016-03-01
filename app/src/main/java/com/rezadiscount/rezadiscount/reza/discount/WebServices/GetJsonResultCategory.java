package com.rezadiscount.rezadiscount.reza.discount.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.Business.Category;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.CategoryReturn;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import org.json.JSONArray;
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


//TODO refactoring
public class GetJsonResultCategory extends AsyncTask<String, String, JSONObject>

{
    private ProgressDialog pDialog;
    private Context context;
    private HashMap<String, String> listHeaders;
    private String resultJSON;
    private JSONObject json;
    private JSONObject bodyJson;

    private CategoryReturn categoryReturn;

    private List<GetJsonListenerCategory> listeners = new ArrayList<>();

    public void setParams(Context con, HashMap<String, String> listHeadersP, JSONObject body) {
        context = con;
        listHeaders = listHeadersP;
        listHeaders.put(QuickstartPreferences.TAG_ACCEPT, "application/json");
        listHeaders.put(QuickstartPreferences.TAG_CONTENT_TYPE, "application/json");
        listHeaders.put(QuickstartPreferences.TAG_DEVICE_ID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
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

        categoryReturn = new CategoryReturn();

        ArrayList<Category> categoryList = new ArrayList<>();

        try {

            JSONArray jsonArray = json.getJSONArray(QuickstartPreferences.TAG_RESULT);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject c = jsonArray.getJSONObject(i);

                Category category = new Category();
                category.setId(c.getInt(QuickstartPreferences.TAG_ID));
                category.setName(c.getString(QuickstartPreferences.TAG_NAME));

                categoryList.add(category);

            }

            categoryReturn.setListCategories(categoryList);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            for (GetJsonListenerCategory hl : listeners) {
                hl.getReturnCategory();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public JSONObject getJSONFromUrl(HashMap<String, String> headers, JSONObject jsonBody) {

        // Making HTTP request
        try {

            URL url = new URL(QuickstartPreferences.URL_SERV + QuickstartPreferences.URL_CAT);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

            //TODO catch timeout
            httpconn.setConnectTimeout(15000);
            httpconn.setReadTimeout(15000);

            httpconn.setRequestMethod(QuickstartPreferences.TAG_GET);

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
                String fullLine = line + "n";
                sb.append(fullLine);
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

    public void addListener(GetJsonListenerCategory toAdd) {
        listeners.add(toAdd);
    }


    // Method to call when Post execute is done
    public CategoryReturn getReturnCategories() {
        return categoryReturn;
    }

}