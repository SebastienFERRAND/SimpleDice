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
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetDateSpinnerListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SubscribeActivity extends AppCompatActivity implements GetJsonListener, GetDateSpinnerListener {


    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private EditText password;
    private TextView birthday;


    private Button subscribe;

    private GetJsonResult jsonResult;
    private GetJsonListener jsonListener;

    private Activity act;

    private RadioGroup genderRb;
    private RadioButton genderSelected;

    private View.OnClickListener subscribeClick;

    private FragmentDatePickerDialog dateFragment;
    private GetDateSpinnerListener datePickerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        findViewsById();

        setValues();

        subscribe.setOnClickListener(subscribeClick);

    }

    private void setValues() {
        Intent intent = this.getIntent();

        if(intent.getStringExtra(QuickstartPreferences.TAG_LASTNAME)==null){
            password.setVisibility(View.GONE);
        }

        lastName.setText(intent.getStringExtra(QuickstartPreferences.TAG_LASTNAME));
        firstName.setText(intent.getStringExtra(QuickstartPreferences.TAG_FIRSTNAME));
        email.setText(intent.getStringExtra(QuickstartPreferences.TAG_EMAIL));
        password.setText(intent.getStringExtra(QuickstartPreferences.TAG_PASSWD));
        birthday.setText(intent.getStringExtra(QuickstartPreferences.TAG_BIRTHDAY));

    }

    public void findViewsById() {

        act = this;
        jsonListener = this;
        datePickerListener = this;

        lastName = (EditText) findViewById(R.id.lastname);
        firstName = (EditText) findViewById(R.id.firstname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        birthday = (TextView) findViewById(R.id.birthday);
        genderRb = (RadioGroup) findViewById(R.id.gender_radio);

        subscribe = (Button) findViewById(R.id.subscribe);

        /*birthday.setOnClickListener(showDatePicker);


        showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
                Log.d("Test", "Click !");
            }
        };*/



        subscribeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> headerList = new HashMap<String, String>();

                // TODO Remove Lat and Long from this query
                headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");

                JSONObject bodyAuth = new JSONObject();
                JSONObject parent = new JSONObject();

                // get selected radio button from radioGroup
                int selectedId = genderRb.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                genderSelected = (RadioButton) findViewById(selectedId);

                try {
                    bodyAuth.put(QuickstartPreferences.TAG_LASTNAME, lastName.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_FIRSTNAME, firstName.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_EMAIL, email.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_PASSWD, password.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDate(dateFragment.getDate() + "/" +
                            dateFragment.getMonth() + "/" + dateFragment.getYear(), "dd/MM/yyyy"));
                    bodyAuth.put(QuickstartPreferences.TAG_GENDER, genderSelected.getText());
                    parent.put("register", bodyAuth);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonResult = new GetJsonResult();
                jsonResult.setParams(act, headerList, QuickstartPreferences.URL_REG, QuickstartPreferences.TAG_POST, parent);
                jsonResult.addListener(jsonListener);
                jsonResult.execute();
            }
        };
    }

    @Override
    public void getJsonObject() {

        Log.d("Test", jsonResult.getJson().toString());

    }

    private void showDatePickerDialog(View v) {
        dateFragment = new FragmentDatePickerDialog();
        dateFragment.addListener(datePickerListener);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void getDateSpinner() {

        birthday.setText(dateFragment.getDate() + " " +
                QuickstartPreferences.getMonth(dateFragment.getMonth()) + " " + dateFragment.getYear());
    }

    public void showDatePicker(View v){
        showDatePickerDialog(v);
        Log.d("Test", "Click !");
    }

}
