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

    public static String getToken() {
        return sharedPref.getString("token", "");
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String getGCMToken() {


        if (GoogleGCM.checkPlayServices(act)) {
            // Start IntentService to register this application with GCM.
            Log.d("GCM", " Registering service ");
            Intent intent = new Intent(act, RegistrationIntentService.class);
            act.startService(intent);
        }
        Log.d("GCM", " get GCM token :" + sharedPref.getString("tokenGCM", ""));
        return sharedPref.getString("tokenGCM", "");
    }

    public static void setGCMToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("tokenGCM", token);
        editor.commit();
    }


}
