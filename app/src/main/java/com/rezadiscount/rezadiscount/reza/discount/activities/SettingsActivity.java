package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.LoginButton;
import com.rezadiscount.rezadiscount.R;

public class SettingsActivity extends Activity {


    private LoginButton loginButton;

    private AccessTokenTracker fbTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }
}
