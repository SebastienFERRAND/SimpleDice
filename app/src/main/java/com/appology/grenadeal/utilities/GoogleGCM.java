package com.appology.grenadeal.utilities;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by sebastienferrand on 12/20/15.
 */
public class GoogleGCM {

    public static boolean checkPlayServices(Activity act) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(act);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(act, resultCode, QuickstartPreferences.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM", "This device is not supported.");
                act.finish();
            }
            Log.i("GCM", "This device is not supported.");
            return false;
        }
        Log.i("GCM", "This device is supported.");
        return true;
    }
}
