package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.rezadiscount.rezadiscount.reza.discount.services.RegistrationIntentService;

/**
 * Created by Sébastien on 11/18/2015.
 */
public final class SharedPreferencesModule {

    private static String sharedPrefName = "sharedPrefFile";
    private static Activity act;
    private static SharedPreferences sharedPref;

    private SharedPreferencesModule() {

    }

    public static void initialise(Activity activity) {
        act = activity;
        sharedPref = act.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    public static void setToken(String token) {
        if (sharedPref == null) {

        } else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", token);
            editor.commit();
        }
    }

    public static String getToken() {
        if (sharedPref == null) {
            return "null";
        } else {
            return sharedPref.getString("token", "");
        }
    }

    public static void setGCMToken(String token) {
        if (sharedPref == null) {

        } else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("tokenGCM", token);
            editor.commit();
        }
    }

    public static String getGCMToken() {


        if (GoogleGCM.checkPlayServices(act)) {
            // Start IntentService to register this application with GCM.
            //Intent intent = new Intent(this, RegistrationIntentService.class);
            //startService(intent);
            Intent intent = new Intent(act, RegistrationIntentService.class);
            act.startService(intent);
        }


        if (sharedPref == null) {
            return "";
        } else {
            Log.d("GCM", " get GCM token :" + sharedPref.getString("tokenGCM", ""));
            return sharedPref.getString("tokenGCM", "");
        }
    }


}
