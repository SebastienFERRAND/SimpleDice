package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessResults extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    //private static String url = "http://lucas.touratier.fr/api.php";
    private static String url = "http://api.booking.touratier.fr/merchant";

    private ListView list;

    private double latitude;
    private double longitude;

    //JSON Node Names
    private static final String TAG_OS = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_LABEL = "name";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_PICTURE = "picture";
    private static final String TAG_ADRESS = "adress";

    JSONArray android = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Menu Navigation
        setContentView(R.layout.activity_business_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent myIntent = getIntent();

        longitude = myIntent.getDoubleExtra("longitude", 0);
        latitude = myIntent.getDoubleExtra("latitude", 0);

        //Populate Businesses items
        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            JsonParser jParser = new JsonParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
