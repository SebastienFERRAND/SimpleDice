package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.HTTPStandardReturn;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.SignInReturn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerSignIn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerSignUp;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultSignIn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultSignUp;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetDateSpinnerListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivitySignUp extends AppCompatActivity implements GetJsonListenerSignUp, GetJsonListenerSignIn, GetDateSpinnerListener {


    public static final int MALE = 0;
    public static final int FEMALE = 1;
    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private TextView birthday;
    private Button subscribe;
    private GetJsonResultSignUp jsonResultSignUp;
    private GetJsonListenerSignUp jsonListenerSignUp;
    private GetJsonResultSignIn jsonResultSignIn;
    private GetJsonListenerSignIn jsonListenerSignIn;
    private Activity act;
    private RadioGroup genderRg;
    private RadioButton genderSelected;
    private View.OnClickListener subscribeClick;
    private FragmentDatePickerDialog dateFragment;
    private GetDateSpinnerListener datePickerListener;
    private Intent intent;
    private TextView errorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        jsonListenerSignIn = this;
        jsonListenerSignUp = this;
        datePickerListener = this;

        lastName = (EditText) findViewById(R.id.lastname);
        firstName = (EditText) findViewById(R.id.firstname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordRepeat = (EditText) findViewById(R.id.passwd_repeat);
        birthday = (TextView) findViewById(R.id.birthday);
        genderRg = (RadioGroup) findViewById(R.id.gender_radio);
        errorTv = (TextView) findViewById(R.id.error);

        subscribe = (Button) findViewById(R.id.subscribe);

        subscribeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean fieldError = false;


                // Test if all fields are filled up
                if (lastName.getText().toString().equals("")) {
                    lastName.setError(ActivitySignUp.this.getResources().getString(R.string.last_name_empty));
                    fieldError = true;
                }
                if (firstName.getText().toString().equals("")) {
                    firstName.setError(ActivitySignUp.this.getResources().getString(R.string.first_name_empty));
                    fieldError = true;
                }
                if (email.getText().toString().equals("")) {
                    email.setError(ActivitySignUp.this.getResources().getString(R.string.email_empty));
                    fieldError = true;
                }

                if (birthday.getText().toString().equals("")) {
                    birthday.setError(ActivitySignUp.this.getResources().getString(R.string.birthday_empty));
                    fieldError = true;
                }

                // Test email validity
                if (!QuickstartPreferences.isValidEmail(email.getText().toString())) {
                    email.setError(ActivitySignUp.this.getResources().getString(R.string.email_incorrect));
                    fieldError = true;
                }

                //If it's a normal subscription with passwords
                if (!intent.getBooleanExtra(QuickstartPreferences.TAG_ISFB, false)) {
                    // If password won't match
                    if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
                        passwordRepeat.setError(ActivitySignUp.this.getResources().getString(R.string.password_problem_match));
                        fieldError = true;
                    }
                    // If password empty
                    else if (password.getText().toString().isEmpty()) {
                        password.setError(ActivitySignUp.this.getResources().getString(R.string.password_problem_empty));
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
    public void getReturnSignUp() {

        HTTPStandardReturn signUpReturn = jsonResultSignUp.getReturnSignUp();

        // if HTTP code is ok
        if (signUpReturn.getHTTPCode().equals(QuickstartPreferences.TAG_HTTP_SUCCESS)) {

            // Probably a useless check as http_code 200 will always have a code 0
            // If signup was a sucess, then sign in
            if (signUpReturn.getErrorCode().equals(QuickstartPreferences.TAG_NO_ERROR)) {
                Log.d("HTTP", "Subscription success");
                SignInUser();
            }

        } else if (signUpReturn.getHTTPCode().startsWith(QuickstartPreferences.TAG_HTTP_FAIL)) {

            //Checking custom error codes
            switch (signUpReturn.getErrorCode()) {

                // An user is already present in database for this email adress.
                case QuickstartPreferences.TAG_ERROR_CODE_REG_1:
                    errorTv.setText(getResources().getString(R.string.api_error_reg_1));
                    break;
                // "password" property is missing or empty.
                // TODO this should never happen, if it does, think of a way to warn us
                case QuickstartPreferences.TAG_ERROR_CODE_REG_2:
                    errorTv.setText(getResources().getString(R.string.api_error_reg_2));
                    break;
                // "birthday" format or date is invalid.
                // TODO this should never happen, if it does, think of a way to warn us
                case QuickstartPreferences.TAG_ERROR_CODE_REG_3:
                    errorTv.setText(getResources().getString(R.string.api_error_reg_3));
                    break;
                // "password" lenght is invalid.
                // TODO this should never happen, if it does, think of a way to warn us
                case QuickstartPreferences.TAG_ERROR_CODE_REG_4:
                    errorTv.setText(getResources().getString(R.string.api_error_reg_4));
                    break;
            }
        } else {
            Log.d("HTTP", "Subscription problems");
        }
    }


    @Override
    public void getReturnSignIn() {

        SignInReturn signInReturn = jsonResultSignIn.getReturnSignIn();

        // if Success
        if (signInReturn.getHTTPCode().equals("200")) {
            Log.d("HTTP", "Connexion success");
            getTokenAndSignIn();
        } else {
            Log.d("HTTP", "Connexion problems");
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

        jsonResultSignUp = new GetJsonResultSignUp();
        jsonResultSignUp.setParams(act, headerList, parent);
        jsonResultSignUp.addListener(jsonListenerSignUp);
        jsonResultSignUp.execute();


    }

    private void SignInUser() {
        Log.d("HTTP", "Connexion");
        HashMap<String, String> headerList = new HashMap<>();
        SharedPreferencesModule.initialise(this);
        headerList.put(QuickstartPreferences.TAG_TOKENG, SharedPreferencesModule.getGCMToken());


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

        jsonResultSignIn = new GetJsonResultSignIn();
        jsonResultSignIn.setParams(this, headerList, null);
        jsonResultSignIn.addListener(jsonListenerSignIn);
        jsonResultSignIn.execute();
    }

    private void getTokenAndSignIn() {
        Log.d("HTTP", "get token and login");

        SharedPreferencesModule.initialise(this);
        SharedPreferencesModule.setToken(jsonResultSignIn.getReturnSignIn().getToken());

        Intent myIntent = new Intent(ActivitySignUp.this, ActivityDeal.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivitySignUp.this.startActivity(myIntent);
    }
}
