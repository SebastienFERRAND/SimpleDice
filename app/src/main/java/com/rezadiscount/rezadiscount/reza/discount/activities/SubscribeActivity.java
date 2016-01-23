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
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetDateSpinnerListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

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
        if (intent.getStringExtra(QuickstartPreferences.TAG_LASTNAME) != null) {

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
        } else {
            // Set male by default
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

                // If password won't match
                if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.password_problem_match), Toast.LENGTH_LONG).show();
                }
                // If password empty
                else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(SubscribeActivity.this, SubscribeActivity.this.getResources().getString(R.string.password_problem_empty), Toast.LENGTH_LONG).show();
                } else {

                    HashMap<String, String> headerList = new HashMap<>();

                    // TODO Remove Lat and Long from this query
                    headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                    headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");
                    headerList.put(QuickstartPreferences.TAG_TOKENFB, intent.getStringExtra(QuickstartPreferences.TAG_TOKENFB));

                    JSONObject bodyAuth = new JSONObject();
                    JSONObject parent = new JSONObject();

                    // get selected radio button from radioGroup
                    int selectedId = genderRg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    genderSelected = (RadioButton) findViewById(selectedId);

                    try {
                        bodyAuth.put(QuickstartPreferences.TAG_LASTNAME, lastName.getText().toString());
                        bodyAuth.put(QuickstartPreferences.TAG_FIRSTNAME, firstName.getText().toString());
                        bodyAuth.put(QuickstartPreferences.TAG_EMAIL, email.getText().toString());
                        bodyAuth.put(QuickstartPreferences.TAG_PASSWD, password.getText().toString());
                        bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDateFormat(birthday.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss"));
                        Log.d("body sub", QuickstartPreferences.convertToDateFormat(birthday.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss"));
                        Log.d("body sub convert", birthday.getText().toString());
                        bodyAuth.put(QuickstartPreferences.TAG_GENDER, radioToValue());
                        bodyAuth.put(QuickstartPreferences.TAG_FBUID, intent.getStringExtra(QuickstartPreferences.TAG_FBUID));
                        parent.put("register", bodyAuth);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonResult = new GetJsonResult();
                    jsonResult.setParams(act, headerList, QuickstartPreferences.URL_REG, QuickstartPreferences.TAG_POST, parent);
                    jsonResult.addListener(jsonListener);
                    jsonResult.execute();
                }

            }
        };
    }

    @Override
    public void getJsonObject() {

        Log.d("JSON", jsonResult.getJson().toString());

        String code_retour = "";
        try {
            code_retour = jsonResult.getJson().getString(QuickstartPreferences.TAG_HTTPCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (code_retour.equals("200")) {
            Intent myIntent = new Intent(SubscribeActivity.this, DealActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SubscribeActivity.this.startActivity(myIntent);
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

    public void showDatePicker(View v){
        showDatePickerDialog(v);
    }

    public String radioToValue(){
        if (genderSelected.getText().equals(this.getResources().getString(R.string.female))){
            return "F";
        }
        if (genderSelected.getText().equals(this.getResources().getString(R.string.male))){
            return "M";
        }

        return null;

    }

}
