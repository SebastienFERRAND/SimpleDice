package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.HTTPStandardReturn;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.SignInReturn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerSignIn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerSignUp;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultSignIn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultSignUp;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements GetJsonListenerSignIn, GetJsonListenerSignUp {

    private Button connexion;
    private Button register;
    private Button passwordForgotten;

    private EditText connexionField;
    private EditText passwordField;
    private LoginButton loginButton;
    private TextView errorTv;

    private JSONObject jsonConnexionOrResult = null;
    private Context context;

    private GetJsonListenerSignIn jsonListenerSignIn;
    private GetJsonResultSignIn jsonResultSignIn;

    private GetJsonListenerSignUp jsonListenerSignUp;
    private GetJsonResultSignUp jsonResultSignUp;

    private CallbackManager callbackManager;

    private AccessToken tokenF;

    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String birthday;
    private String genderSelected;

    private Activity activity;

    private View.OnClickListener subscribeAction;
    private View.OnClickListener connectionAction;

    private String source = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //if a token already exist, no need to sign in
        SharedPreferencesModule.initialise(this);
        if (!SharedPreferencesModule.getToken().equals("")) {
            Intent myIntent = new Intent(SignInActivity.this, DealActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SignInActivity.this.startActivity(myIntent);
        }

        QuickstartPreferences.URL_SERV = QuickstartPreferences.URL_SERV_DEV;

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_sign_in_up);

        callbackManager = CallbackManager.Factory.create();

        findViewsById();


        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            Log.d("facebook", "Token facebook " + token);
        } else {
            Log.d("facebook", "no token ");
        }

        List<String> permissionList = new ArrayList<>();
        permissionList.add("public_profile");
        permissionList.add("email");
        permissionList.add("user_birthday");
        permissionList.add("user_friends");

        loginButton.setReadPermissions(permissionList);
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

                                            id = object.getString("id");
                                            lastName = object.getString("last_name");
                                            firstName = object.getString("first_name");
                                            // Email can be unavailable
                                            email = object.optString("email", "");
                                            // Birthday can be unavailable
                                            birthday = object.optString("birthday", null);
                                            genderSelected = object.getString("gender");

                                            facebookSignIn();

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
                        Log.d("Facebook", "Cancel ");
                        errorTv.setText(activity.getResources().getString(R.string.facebook_error));

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("Facebook", "Error " + exception.getMessage());
                        errorTv.setText(activity.getResources().getString(R.string.facebook_error));

                    }
                });


        // GET SSH KEY
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                try {
                    MessageDigest md;
                    md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash", e.getMessage());
        }

        connexion.setOnClickListener(connectionAction);
        register.setOnClickListener(subscribeAction);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // GCM Token result
        if (data != null) {
            super.onActivityResult(requestCode, resultCode, data);
            //Call the facebook activity result
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Log.d("LoginActivity", "Activity result " + requestCode + resultCode + data.toString());
        } else {
            Log.d("GCM", "Data is null");
        }

    }

    @Override
    public void getReturnSignIn() {

        SignInReturn signInReturn = jsonResultSignIn.getReturnSignIn();

        if (source.equals(QuickstartPreferences.facebookConnexion)) {
            source = "";
            Log.d("HTTP", "Connexion Facebook");
            // if Success
            if (signInReturn.getCode().equals("200")) {
                Log.d("HTTP", "Connexion Facebook success");
                getTokenAndLogin();

            } else { // Failure facebook connexion
                Log.d("HTTP", "Connexion Facebook fail");

                LoginManager.getInstance().logOut();
                SharedPreferencesModule.setToken("");

                if ((lastName == null) ||
                        (firstName == null) ||
                        (email == null) ||
                        (birthday == null) ||
                        (genderSelected == null)) { //if some info are missing then send him to Subscribe activity to complete it
                    Log.d("Facebook", "Missing infos");

                    facebookSignUpMissingInfo();
                } else {
                    //Subscribe with facebook
                    facebookSignUp();
                }
            }
        } else if (source.equals(QuickstartPreferences.normalConnexion)) {
            source = "";
            Log.d("HTTP", "Connexion Normale");
            // if Success
            if (signInReturn.getCode().equals("200")) {
                Log.d("HTTP", "Normal connexion success");
                getTokenAndLogin();
            } else {
                errorTv.setText(activity.getResources().getString(R.string.connexion_problem));
            }
        }
    }

    //Sign up facebook
    @Override
    public void getReturnSignUp() {

        HTTPStandardReturn signUpReturn = jsonResultSignUp.getReturnSignUp();

        // If signup was a sucess, then sign in
        if (signUpReturn.getCode().equals("200")) {
            facebookSignIn();
        } else {
            Toast.makeText(this, "There was an error on signup", Toast.LENGTH_LONG);
        }
    }

    private void findViewsById() {
        context = this;
        jsonListenerSignIn = this;
        jsonListenerSignUp = this;

        loginButton = (LoginButton) findViewById(R.id.login_button);
        connexion = (Button) findViewById(R.id.connexion);
        register = (Button) findViewById(R.id.inscription);
        passwordForgotten = (Button) findViewById(R.id.password_forgotten);
        connexionField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.passwd_field);
        errorTv = (TextView) findViewById(R.id.error);
        activity = this;

        passwordForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInActivity.this, PasswordActivity.class);
                SignInActivity.this.startActivity(myIntent);
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.url_rb);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dev_rb) {
                    Log.d("ID Radio", "0 dev");
                    QuickstartPreferences.URL_SERV = QuickstartPreferences.URL_SERV_DEV;
                } else if (checkedId == R.id.preprod_rb) {
                    Log.d("ID Radio", "1 preprod");
                    QuickstartPreferences.URL_SERV = QuickstartPreferences.URL_SERV_PREPROD;
                }
            }
        });

        subscribeAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                SignInActivity.this.startActivity(myIntent);
            }
        };

        // Normal Authentication
        connectionAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalSignIn();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If coming from password recovery, show alertDialog of success
        Intent myIntent = getIntent();
        if (myIntent.getBooleanExtra("fromRecovery", false)) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.password_email_success);
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        }

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


    private void normalSignIn() {

        boolean missingArgument = false;

        if (connexionField.getText().toString().equals("")) {
            connexionField.setError(activity.getResources().getString(R.string.email_empty));
            missingArgument = true;
        }

        if (passwordField.getText().toString().equals("")) {
            passwordField.setError(activity.getResources().getString(R.string.password_problem_empty));
            missingArgument = true;
        }

        if (!missingArgument) {
            // Getting JSON from URL
            HashMap<String, String> headerList = new HashMap<>();
            headerList.put(QuickstartPreferences.TAG_LOGIN, connexionField.getText().toString());
            headerList.put(QuickstartPreferences.TAG_PASSWD, passwordField.getText().toString());
            SharedPreferencesModule.initialise(activity);
            headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
            headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

            jsonResultSignIn = new GetJsonResultSignIn();
            jsonResultSignIn.setParams(context, headerList, null);
            jsonResultSignIn.addListener(jsonListenerSignIn);
            jsonResultSignIn.execute();

            source = QuickstartPreferences.normalConnexion;
        }
    }

    private void facebookSignIn() {

        Log.d("Facebook", "Connecting with facebook");

        HashMap<String, String> headerList = new HashMap<>();
        headerList.put(QuickstartPreferences.TAG_FBUID, id);
        headerList.put(QuickstartPreferences.TAG_TOKENFB, tokenF.getToken());
        SharedPreferencesModule.initialise(activity);
        headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
        headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);

        jsonResultSignIn = new GetJsonResultSignIn();
        jsonResultSignIn.setParams(context, headerList, null);
        jsonResultSignIn.addListener(jsonListenerSignIn);
        jsonResultSignIn.execute();

        source = QuickstartPreferences.facebookConnexion;
    }


    private void facebookSignUp() {

        Log.d("Facebook", "Facebook subscription");
        //if we have all user info we need from facebook then send Subscribe request

        // Subscription
        HashMap<String, String> headerList = new HashMap<>();

        JSONObject bodyAuth = new JSONObject();
        JSONObject parent = new JSONObject();

        headerList.put(QuickstartPreferences.TAG_TOKENFB, tokenF.getToken());

        try {
            bodyAuth.put(QuickstartPreferences.TAG_FBUID, id);
            bodyAuth.put(QuickstartPreferences.TAG_LASTNAME, lastName);
            bodyAuth.put(QuickstartPreferences.TAG_FIRSTNAME, firstName);
            bodyAuth.put(QuickstartPreferences.TAG_EMAIL, email);
            bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDateFormat(birthday, "MM/dd/yyyy", "yyyy-MM-dd hh:mm:ss"));
            Log.d("Birthday", birthday);
            bodyAuth.put(QuickstartPreferences.TAG_GENDER, genderSelected.substring(0, 1).toUpperCase());
            parent.put("register", bodyAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonResultSignUp = new GetJsonResultSignUp();
        jsonResultSignUp.setParams(this, headerList, parent);
        jsonResultSignUp.addListener(jsonListenerSignUp);
        jsonResultSignUp.execute();

    }


    private void facebookSignUpMissingInfo() {
        Intent myIntent = new Intent(SignInActivity.this, SignUpActivity.class);

        myIntent.putExtra(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDateFormat(birthday, "MM/dd/yyyy", "yyyy-MM-dd hh:mm:ss"));
        myIntent.putExtra(QuickstartPreferences.TAG_LASTNAME, lastName);
        myIntent.putExtra(QuickstartPreferences.TAG_FIRSTNAME, firstName);
        myIntent.putExtra(QuickstartPreferences.TAG_EMAIL, email);
        myIntent.putExtra(QuickstartPreferences.TAG_GENDER, genderSelected);
        myIntent.putExtra(QuickstartPreferences.TAG_FBUID, id);
        myIntent.putExtra(QuickstartPreferences.TAG_TOKENFB, tokenF.getToken());
        myIntent.putExtra(QuickstartPreferences.TAG_ISFB, true);

        SignInActivity.this.startActivity(myIntent);
    }

    private void getTokenAndLogin() {
        Log.d("Token", "get token and login");

        SignInReturn signInReturn = jsonResultSignIn.getReturnSignIn();

        // Store it in local
        SharedPreferencesModule.initialise(this);
        SharedPreferencesModule.setToken(signInReturn.getToken());

        // Start Home activity
        Intent myIntent = new Intent(SignInActivity.this, DealActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SignInActivity.this.startActivity(myIntent);
    }


}
