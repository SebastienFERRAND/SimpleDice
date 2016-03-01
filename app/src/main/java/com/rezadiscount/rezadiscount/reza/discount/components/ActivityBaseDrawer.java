package com.rezadiscount.rezadiscount.reza.discount.components;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.activities.ActivityAgenda;
import com.rezadiscount.rezadiscount.reza.discount.activities.ActivityBaseDrawerDeal;
import com.rezadiscount.rezadiscount.reza.discount.activities.ActivityBaseDrawerFind;
import com.rezadiscount.rezadiscount.reza.discount.activities.ActivitySignIn;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

public class ActivityBaseDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView appVersion;

    protected void onCreateBaseDrawerActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Implement header
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        // Get version number
        appVersion = (TextView) headerView.findViewById(R.id.app_version);

        PackageManager manager = this.getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(
                    this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appVersion.setText(version);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.deal) {
            Intent myIntent = new Intent(ActivityBaseDrawer.this, ActivityBaseDrawerDeal.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityBaseDrawer.this.startActivity(myIntent);
        } else if (id == R.id.find) {
            Intent myIntent = new Intent(ActivityBaseDrawer.this, ActivityBaseDrawerFind.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityBaseDrawer.this.startActivity(myIntent);
        } else if (id == R.id.agenda) {
            Intent myIntent = new Intent(ActivityBaseDrawer.this, ActivityAgenda.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityBaseDrawer.this.startActivity(myIntent);
        } else if (id == R.id.logout) {
            LoginManager.getInstance().logOut();
            SharedPreferencesModule.setToken("");
            Intent myIntent = new Intent(this, ActivitySignIn.class);
            this.startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateBaseDrawerActivity();
    }


}
