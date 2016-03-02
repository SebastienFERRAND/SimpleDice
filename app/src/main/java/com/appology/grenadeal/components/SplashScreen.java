package com.appology.grenadeal.components;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.appology.grenadeal.Errors.SplashScreenException;
import com.appology.grenadeal.R;

/**
 * Created by sebastienferrand on 2/26/16.
 * This class gives methods for showing a splash screen
 * It has few methods for different needs
 * TODO Do not forget to surround your whole activity.xml with a RelativeLayout with an id = "splash_screen_root"
 * TODO And use an ImageView with id = "splash_screen" a width and height values to match_parent as the first child component (Visible or not visible depending on your needs)
 * TODO Also, use setActivity(Activity act) before anything else
 */
public class SplashScreen {

    private RelativeLayout rootLayout;

    public void setActivity(Activity activity) throws SplashScreenException {
        try {
            rootLayout = (RelativeLayout) activity.findViewById(R.id.splash_screen_root);
        } catch (Exception e) {
            throw new SplashScreenException("You didn't declare your XML RelativeLayout properly. See class description");
        }
    }

    /**
     * You have to set the context for the splash screen to be usable
     * For that, call setContext();
     */
    private void showSplashScreen() throws SplashScreenException {
        try {
            rootLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            throw new SplashScreenException("You didn't use setActivity() before using showSplashScreen(). See class description");
        }
    }

    /**
     * You have to set the context for the splash screen to be usable
     * For that, call setContext();
     */
    private void hideSplashScreen() throws SplashScreenException {
        try {
            rootLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            throw new SplashScreenException("You didn't use setActivity() before using hideSplashScreen(). See class description");
        }
    }

    /**
     * You have to set the context for the splash screen to be usable
     * For that, call setContext();
     */
    //TODO Implement with a sleep
    private void showSplashScreenForAWhile() {

    }

}
