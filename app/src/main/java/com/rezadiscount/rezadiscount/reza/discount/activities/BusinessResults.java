package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.components.BaseDrawerActivity;
import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonHTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessResults extends BaseDrawerActivity {

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    //private static String url = "http://lucas.touratier.fr/api.php";
    private static String url = "merchant";

    private ListView list;

    //JSON Node Names
    private static final String TAG_RESULT = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_LABEL = "name";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_PICTURE = "picture";
    private static final String TAG_ADRESS = "adress";

    //if error HTTP

    private static final String TAG_TYPE = "type";
    private static final String TAG_TITLE = "title";
    private static final String TAG_STATUS = "status";
    private static final String TAG_DETAIL = "detail";


    JSONArray android = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Menu Navigation
        setContentView(R.layout.activity_business_results);

        //Populate Businesses items
        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessResults.this);
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
            headerList.put("token", "123123qdsq");
            headerList.put("deviceid",  Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url_api) + url, headerList, "GET");

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                if (json.getJSONArray(TAG_RESULT) != null){

                    android = json.getJSONArray(TAG_RESULT);
                    for(int i = 0; i < android.length(); i++){
                        JSONObject c = android.getJSONObject(i);

                        // Storing  JSON item in a Variable
                        String idS = c.getString(TAG_ID);
                        String labelS = c.getString(TAG_LABEL);
                        String latitudeS = c.getString(TAG_latitude);
                        String longitudeS = c.getString(TAG_LONGITUDE);
                        String distanceS = c.getString(TAG_DISTANCE);
                        String pictureS = c.getString(TAG_PICTURE);
                        String adressS = c.getString(TAG_ADRESS);

                        // Adding value HashMap key => value

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_ID, idS);
                        map.put(TAG_LABEL, labelS);
                        map.put(TAG_latitude, latitudeS);
                        map.put(TAG_LONGITUDE, longitudeS);
                        map.put(TAG_DISTANCE, distanceS);
                        map.put(TAG_PICTURE, pictureS);
                        map.put(TAG_ADRESS, adressS);

                        oslist.add(map);
                        list=(ListView)findViewById(R.id.list);

                        ListAdapter adapter = new SimpleAdapter(BusinessResults.this, oslist,
                                R.layout.business_row_item,
                                new String[] { TAG_ID, TAG_LABEL, TAG_DISTANCE}, new int[] {
                                R.id.id, R.id.label, R.id.distance});

                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent myIntent = new Intent(BusinessResults.this, BusinessProfile.class);

                                myIntent.putExtra("id", oslist.get(position).get("id"));
                                myIntent.putExtra("label", oslist.get(position).get("label"));
                                myIntent.putExtra("latitude", oslist.get(position).get("latitude"));
                                myIntent.putExtra("longitude", oslist.get(position).get("longitude"));
                                myIntent.putExtra("distance", oslist.get(position).get("distance"));
                                myIntent.putExtra("picture", oslist.get(position).get("picture"));
                                myIntent.putExtra("adress", oslist.get(position).get("adress"));

                                BusinessResults.this.startActivity(myIntent);
                            }
                        });
                    }
                }else{
                    Log.d("Test", " BUG test ");
                }

            } catch (JSONException e) {


                try {
                    String status = json.getString(TAG_STATUS);
                    String details = json.getString(TAG_DETAIL);

                    Toast.makeText(getApplicationContext(), "There was an error " + status + " : " + details, Toast.LENGTH_LONG).show();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }
    }
}
