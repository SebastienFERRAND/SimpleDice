package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.LoginButton;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

public class SettingsActivity extends Activity {


    private LoginButton loginButton;

    private AccessTokenTracker fbTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d("FB", "User Logged Out.");
                    SharedPreferencesModule.setToken("");
                    Intent myIntent = new Intent(SettingsActivity.this, SignInUp.class);
                    SettingsActivity.this.startActivity(myIntent);
                }
            }
        };


    }
}
