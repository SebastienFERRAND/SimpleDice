package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rezadiscount.rezadiscount.R;

import com.rezadiscount.rezadiscount.reza.discount.services.RegistrationIntentService;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import com.facebook.FacebookSdk;

public class SignInUp extends AppCompatActivity implements GetJsonListener {

    private Button connexion;
    private Button inscription;
    private EditText connexionField;
    private EditText passwordField;
    private JSONObject androidV = null;
    private Context context;
    private GetJsonListener jsonListener;
    private GetJsonResult jsonResult;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in_up);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("Tokenfb:", loginResult.getAccessToken().getToken());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        // GET SSH KEY
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash1:", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash2:", e.getMessage());
        }

        context = this;
        jsonListener = this;

        connexion = (Button) findViewById(R.id.connexion);
        inscription = (Button) findViewById(R.id.inscription);

        connexionField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.passwd_field);


        //if a token already exist, no need to sign in
        SharedPreferencesModule.initialise(getApplicationContext());
        if (!SharedPreferencesModule.getToken().equals("")) {
            Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SignInUp.this.startActivity(myIntent);
        }

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting JSON from URL
                HashMap<String, String> headerList = new HashMap<String, String>();
                headerList.put(QuickstartPreferences.TAG_LOGIN, connexionField.getText().toString());
                headerList.put(QuickstartPreferences.TAG_PASSWD, passwordField.getText().toString());
                headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
                SharedPreferencesModule.initialise(getApplicationContext());
                headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
                headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

                jsonResult = new GetJsonResult();
                jsonResult.setParams(context, headerList, QuickstartPreferences.url_auth, "GET");
                jsonResult.addListener(jsonListener);
                jsonResult.execute();

            }
        });

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInUp.this, SubscribeActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SignInUp.this.startActivity(myIntent);
            }
        });

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            //Intent intent = new Intent(this, RegistrationIntentService.class);
            //startService(intent);
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getJsonObject() {
        try {
            androidV = jsonResult.getJson().getJSONObject(QuickstartPreferences.TAG_RESULT);

            // Storing  JSON item in a Variable
            String token = androidV.getString(QuickstartPreferences.TAG_TOKEN);

            SharedPreferencesModule.initialise(getApplicationContext());
            SharedPreferencesModule.setToken(token);

            Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SignInUp.this.startActivity(myIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Test", "This device is not supported.");
                finish();
            }
            Log.i("Test", "This device is not supported.");
            return false;
        }
        Log.i("Test", "This device is supported.");
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}
