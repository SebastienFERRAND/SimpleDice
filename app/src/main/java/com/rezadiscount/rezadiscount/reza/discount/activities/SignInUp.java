package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rezadiscount.rezadiscount.R;
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

public class SignInUp extends AppCompatActivity implements GetJsonListener {

    private Button connexion;
    private Button register;
    private EditText connexionField;
    private EditText passwordField;
    private LoginButton loginButton;

    private JSONObject jsonConnexionOrResult = null;
    private Context context;
    private GetJsonListener jsonListener;
    private GetJsonResult jsonResult;
    private CallbackManager callbackManager;

    private AccessToken tokenF;

    private boolean isFbConnection;

    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String birthday;
    private String genderSelected;

    private Activity activity;

    private View.OnClickListener subscribeAction;
    private View.OnClickListener connectionAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //if a token already exist, no need to sign in
        SharedPreferencesModule.initialise(this);
        if (!SharedPreferencesModule.getToken().equals("")) {
            Intent myIntent = new Intent(SignInUp.this, DealActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SignInUp.this.startActivity(myIntent);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_sign_in_up);

        findViewsById();

        callbackManager = CallbackManager.Factory.create();

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            Log.d("facebook", "Token facebook " + token);
        } else {
            Log.d("facebook", "no token ");
        }

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        Log.d("Facebook", "Login success");

                        tokenF = loginResult.getAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        try {
                                            isFbConnection = true;

                                            Log.d("Facebook", "id: " + object.getString("id"));
                                            Log.d("Facebook", "lastName: " + object.getString("last_name"));
                                            Log.d("Facebook", "firstName: " + object.getString("first_name"));
                                            Log.d("Facebook", "email: " + object.getString("email"));
                                            Log.d("Facebook", "birthday: " + object.optString("birthday", null));
                                            Log.d("Facebook", "genderSelected: " + object.getString("gender"));

                                            id = object.getString("id");
                                            lastName = object.getString("last_name");
                                            firstName = object.getString("first_name");
                                            // Email can be unavailable
                                            email = object.optString("email", "");
                                            // Birthday can be unavailable
                                            birthday = object.optString("birthday", null);
                                            genderSelected = object.getString("gender");

                                            HashMap<String, String> headerList = new HashMap<>();
                                            headerList.put(QuickstartPreferences.TAG_FBUID, id);
                                            headerList.put(QuickstartPreferences.TAG_TOKENFB, tokenF.getToken());
                                            // TODO Remove Lat and Long from this query
                                            headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                                            headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
                                            SharedPreferencesModule.initialise(activity);
                                            headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
                                            headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

                                            jsonResult = new GetJsonResult();
                                            jsonResult.setParams(context, headerList, QuickstartPreferences.URL_AUTH, QuickstartPreferences.TAG_GET, null);
                                            jsonResult.addListener(jsonListener);
                                            jsonResult.execute();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("Facebook", "Cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("Facebook", "Error" + exception.getMessage());

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
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash", e.getMessage());
        }

        connexion.setOnClickListener(connectionAction);
        register.setOnClickListener(subscribeAction);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("LoginActivity", "Activity result " + requestCode + resultCode + data.toString());

    }

    @Override
    public void getJsonObject() {

        if (jsonResult != null) {

            // Connexion
            try {
                // If result is a connection
                jsonConnexionOrResult = jsonResult.getJson().getJSONObject(QuickstartPreferences.TAG_RESULT);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Connexion
            if (jsonConnexionOrResult != null) {
                Log.d("Facebook", "Connec");

                String token = null;
                try {
                    token = jsonConnexionOrResult.getString(QuickstartPreferences.TAG_TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferencesModule.initialise(this);
                SharedPreferencesModule.setToken(token);

                Intent myIntent = new Intent(SignInUp.this, DealActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SignInUp.this.startActivity(myIntent);
            } else {
                Log.d("Facebook", "Inscription");
                // Subscription

                //TODO Ask for return code and perform else if
                String code_retour = "";
                String message = "";
                try {
                    code_retour = jsonResult.getJson().getString(QuickstartPreferences.TAG_HTTPCODE);
                    message = jsonResult.getJson().getString(QuickstartPreferences.TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Inscription facebook
                if (code_retour.equals("401") && (isFbConnection)) {

                    Log.d("Error message", message);

                    isFbConnection = false;

                    Intent myIntent = new Intent(SignInUp.this, SubscribeActivity.class);

                    myIntent.putExtra(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDateFormat(birthday, "MM/dd/yyyy", "yyyy-MM-dd hh:mm:ss"));
                    myIntent.putExtra(QuickstartPreferences.TAG_LASTNAME, lastName);
                    myIntent.putExtra(QuickstartPreferences.TAG_FIRSTNAME, firstName);
                    myIntent.putExtra(QuickstartPreferences.TAG_EMAIL, email);
                    myIntent.putExtra(QuickstartPreferences.TAG_GENDER, genderSelected);
                    myIntent.putExtra(QuickstartPreferences.TAG_FBUID, id);
                    myIntent.putExtra(QuickstartPreferences.TAG_TOKENFB, tokenF.getToken());

                    SignInUp.this.startActivity(myIntent);

                } else {
                    Log.d("Error", message);
                }

            }
        }
    }

    private void findViewsById() {
        context = this;
        jsonListener = this;

        loginButton = (LoginButton) findViewById(R.id.login_button);
        connexion = (Button) findViewById(R.id.connexion);
        register = (Button) findViewById(R.id.inscription);
        connexionField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.passwd_field);
        activity = this;

        subscribeAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInUp.this, SubscribeActivity.class);
                SignInUp.this.startActivity(myIntent);
            }
        };

        connectionAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting JSON from URL
                HashMap<String, String> headerList = new HashMap<>();
                headerList.put(QuickstartPreferences.TAG_LOGIN, connexionField.getText().toString());
                headerList.put(QuickstartPreferences.TAG_PASSWD, passwordField.getText().toString());
                // TODO Remove Lat and Long from this query
                headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
                SharedPreferencesModule.initialise(activity);
                headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
                headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

                jsonResult = new GetJsonResult();
                jsonResult.setParams(context, headerList, QuickstartPreferences.URL_AUTH, QuickstartPreferences.TAG_GET, null);
                jsonResult.addListener(jsonListener);
                jsonResult.execute();

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Initializing google token
        SharedPreferencesModule.getGCMToken();

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
