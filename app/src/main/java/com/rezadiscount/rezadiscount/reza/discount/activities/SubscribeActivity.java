package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetDateSpinnerListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SubscribeActivity extends AppCompatActivity implements GetJsonListener, GetDateSpinnerListener {


    public static final int MALE = 0;
    public static final int FEMALE = 1;
    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private TextView birthday;
    private Button subscribe;
    private GetJsonResult jsonResult;
    private GetJsonListener jsonListener;
    private Activity act;
    private RadioGroup genderRg;
    private RadioButton genderSelected;
    private View.OnClickListener subscribeClick;
    private FragmentDatePickerDialog dateFragment;
    private GetDateSpinnerListener datePickerListener;
    private Intent intent;
    private String source = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        findViewsById();

        setValues();

        subscribe.setOnClickListener(subscribeClick);

    }

    private void setValues() {
        intent = this.getIntent();

        // If facebook sub, no password needed.
        if (intent.getBooleanExtra(QuickstartPreferences.TAG_ISFB, false)) {

            // Pre-setting of user data from facebook
            lastName.setText(intent.getStringExtra(QuickstartPreferences.TAG_LASTNAME));
            firstName.setText(intent.getStringExtra(QuickstartPreferences.TAG_FIRSTNAME));
            email.setText(intent.getStringExtra(QuickstartPreferences.TAG_EMAIL));
            birthday.setText(QuickstartPreferences.convertToDateFormat(intent.getStringExtra(QuickstartPreferences.TAG_BIRTHDAY), "yyyy-MM-dd hh:mm:ss", "dd/MM/yyyy"));


            if (intent.getStringExtra(QuickstartPreferences.TAG_GENDER).equals("male")) {
                ((RadioButton) genderRg.getChildAt(MALE)).setChecked(true);
            } else if (intent.getStringExtra(QuickstartPreferences.TAG_GENDER).equals("female")) {
                ((RadioButton) genderRg.getChildAt(FEMALE)).setChecked(true);
            }

            password.setVisibility(View.GONE);
            passwordRepeat.setVisibility(View.GONE);

        } else {
            // NOT facebook sub, Set male by default
            ((RadioButton) genderRg.getChildAt(MALE)).setChecked(true);
        }
    }

    public void findViewsById() {

        act = this;
        jsonListener = this;
        datePickerListener = this;

        lastName = (EditText) findViewById(R.id.lastname);
        firstName = (EditText) findViewById(R.id.firstname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordRepeat = (EditText) findViewById(R.id.passwd_repeat);
        birthday = (TextView) findViewById(R.id.birthday);
        genderRg = (RadioGroup) findViewById(R.id.gender_radio);

        subscribe = (Button) findViewById(R.id.subscribe);

        subscribeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean fieldError = false;


                // Test if all fields are filled up
                if (lastName.getText().toString().equals("")) {
                    lastName.setError(SubscribeActivity.this.getResources().getString(R.string.last_name_empty));
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.last_name_empty), Toast.LENGTH_LONG).show();
                    fieldError = true;
                }
                if (firstName.getText().toString().equals("")) {
                    firstName.setError(SubscribeActivity.this.getResources().getString(R.string.first_name_empty));
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.first_name_empty), Toast.LENGTH_LONG).show();
                    fieldError = true;
                }
                if (email.getText().toString().equals("")) {
                    email.setError(SubscribeActivity.this.getResources().getString(R.string.email_empty));
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.email_empty), Toast.LENGTH_LONG).show();
                    fieldError = true;
                }
                if (birthday.getText().toString().equals("")) {
                    birthday.setError(SubscribeActivity.this.getResources().getString(R.string.birthday_empty));
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.birthday_empty), Toast.LENGTH_LONG).show();
                    fieldError = true;
                }

                if (!intent.getBooleanExtra(QuickstartPreferences.TAG_ISFB, false)) {
                    // If password won't match
                    if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
                        passwordRepeat.setError(SubscribeActivity.this.getResources().getString(R.string.password_problem_match));
                        Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.password_problem_match), Toast.LENGTH_LONG).show();
                        fieldError = true;
                    }
                    // If password empty
                    else if (password.getText().toString().isEmpty()) {
                        password.setError(SubscribeActivity.this.getResources().getString(R.string.password_problem_empty));
                        Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.password_problem_empty), Toast.LENGTH_LONG).show();
                        fieldError = true;
                    }
                }

                // If there's no errors
                if (!fieldError) {
                    subscribeUser();
                }
            }
        };
    }

    @Override
    public void getJsonObject() {

        Log.d("HTTP", "Result");

        // if Json return isn't null
        if (jsonResult != null) {
            Log.d("HTTP", "Result not null. Source : " + source);
            String code_retour = "";

            // If subscription
            try {
                code_retour = jsonResult.getJson().getString(QuickstartPreferences.TAG_HTTPCODE);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (source.equals(QuickstartPreferences.connexion)) {

                source = "";
                // if Success
                if (code_retour.equals("200")) {
                    Log.d("HTTP", "Connexion success");
                    getTokenAndConnect();
                } else {
                    Log.d("HTTP", "Connexion problems");
                }
            } else if (source.equals(QuickstartPreferences.subscription)) {
                source = "";
                Log.d("HTTP", "Subscription");
                // if Success
                if (code_retour.equals("200")) {
                    Log.d("HTTP", "Subscription success");
                    connectUser();
                } else {
                    Log.d("HTTP", "Subscription problems");
                }
            }
        }
    }

    private void showDatePickerDialog(View v) {
        dateFragment = new FragmentDatePickerDialog();
        dateFragment.addListener(datePickerListener);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void getDateSpinner() {
        birthday.setText(dateFragment.getDate() + "/" +
                dateFragment.getMonth() + "/" + dateFragment.getYear());
    }

    public void showDatePicker(View v) {
        showDatePickerDialog(v);
    }

    public String radioToValue() {
        if (genderSelected.getText().equals(this.getResources().getString(R.string.female))) {
            return "F";
        }
        if (genderSelected.getText().equals(this.getResources().getString(R.string.male))) {
            return "M";
        }
        return null;
    }

    private void subscribeUser() {
        Log.d("HTTP", "Subscription");

        // Subscription
        HashMap<String, String> headerList = new HashMap<>();

        // TODO Remove Lat and Long from this query
        headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");

        JSONObject bodyAuth = new JSONObject();
        JSONObject parent = new JSONObject();

        // get selected radio button from radioGroup
        int selectedId = genderRg.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        genderSelected = (RadioButton) findViewById(selectedId);

        // Facebook Subscribe
        if (intent.getBooleanExtra(QuickstartPreferences.TAG_ISFB, false)) {

            headerList.put(QuickstartPreferences.TAG_TOKENFB, intent.getStringExtra(QuickstartPreferences.TAG_TOKENFB));

            try {
                bodyAuth.put(QuickstartPreferences.TAG_FBUID, intent.getStringExtra(QuickstartPreferences.TAG_FBUID));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Normal subscribe
        } else {

            // Try to send any useful info
            try {
                bodyAuth.put(QuickstartPreferences.TAG_PASSWD, password.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            bodyAuth.put(QuickstartPreferences.TAG_LASTNAME, lastName.getText().toString());
            bodyAuth.put(QuickstartPreferences.TAG_FIRSTNAME, firstName.getText().toString());
            bodyAuth.put(QuickstartPreferences.TAG_EMAIL, email.getText().toString());
            bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDateFormat(birthday.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss"));
            Log.d("body sub", QuickstartPreferences.convertToDateFormat(birthday.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss"));
            Log.d("body sub convert", birthday.getText().toString());
            bodyAuth.put(QuickstartPreferences.TAG_GENDER, radioToValue());

            parent.put(QuickstartPreferences.TAG_REGISTER, bodyAuth);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonResult = new GetJsonResult();
        jsonResult.setParams(act, headerList, QuickstartPreferences.URL_REG, QuickstartPreferences.TAG_POST, parent);
        jsonResult.addListener(jsonListener);
        jsonResult.execute();

        source = QuickstartPreferences.subscription;


    }

    private void connectUser() {
        Log.d("HTTP", "Connexion");
        HashMap<String, String> headerList = new HashMap<>();
        // TODO Remove Lat and Long from this query
        headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
        SharedPreferencesModule.initialise(this);
        headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());
        headerList.put(QuickstartPreferences.TAG_DEVICEMODEL, Build.MANUFACTURER + " " + Build.MODEL);


        // Try connecting with newly created profile
        //If Facebook
        if (intent.getBooleanExtra(QuickstartPreferences.TAG_ISFB, false)) {
            headerList.put(QuickstartPreferences.TAG_FBUID, intent.getStringExtra(QuickstartPreferences.TAG_FBUID));
            headerList.put(QuickstartPreferences.TAG_TOKENFB, intent.getStringExtra(QuickstartPreferences.TAG_TOKENFB));
        } else {
            // if auth
            headerList.put(QuickstartPreferences.TAG_LOGIN, email.getText().toString());
            headerList.put(QuickstartPreferences.TAG_PASSWD, password.getText().toString());
        }

        jsonResult = new GetJsonResult();
        jsonResult.setParams(this, headerList, QuickstartPreferences.URL_AUTH, QuickstartPreferences.TAG_GET, null);
        jsonResult.addListener(jsonListener);
        jsonResult.execute();

        source = QuickstartPreferences.connexion;
    }

    private void getTokenAndConnect() {
        Log.d("HTTP", "get token and login");

        String token = null;
        try {
            token = jsonResult.getJson().getString(QuickstartPreferences.TAG_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferencesModule.initialise(this);
        SharedPreferencesModule.setToken(token);

        Intent myIntent = new Intent(SubscribeActivity.this, DealActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SubscribeActivity.this.startActivity(myIntent);
    }

}
