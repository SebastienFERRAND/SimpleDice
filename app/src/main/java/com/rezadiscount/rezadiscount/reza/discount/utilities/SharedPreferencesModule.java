package com.rezadiscount.rezadiscount.reza.discount.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Sébastien on 11/18/2015.
 */
public final class SharedPreferencesModule {

    private static String sharedPrefName= "sharedPrefFile";
    private static Context con;
    private static SharedPreferences sharedPref;

    private SharedPreferencesModule(){

    }

    public static void initialise(Context context){
        sharedPref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        con = context;
    }

    public static void setToken(String token){
        if (sharedPref == null){

        }else{
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", token);
            editor.commit();
        }
    }

    public static String getToken(){
        if (sharedPref == null){
            return "null";
        }else{
            return sharedPref.getString("token", "");
        }
    }

    public static void setGCMToken(String token){
        if (sharedPref == null){

        }else{
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("tokenGCM", token);
            editor.commit();
        }
    }

    public static String getGCMToken(){
        if (sharedPref == null){
            return "";
        }else{
            return sharedPref.getString("tokenGCM", "");
        }
    }


}
