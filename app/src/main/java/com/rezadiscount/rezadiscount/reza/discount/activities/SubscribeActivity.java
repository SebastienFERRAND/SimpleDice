package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        findViewsById();

        setValues();

        subscribe.setOnClickListener(subscribeClick);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        setDateTimeField();


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

        lastName = (EditText) findViewById(R.id.lastname);
        firstName = (EditText) findViewById(R.id.firstname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        birthday = (TextView) findViewById(R.id.birthday);
        genderRb = (RadioGroup) findViewById(R.id.gender_radio);

        subscribe = (Button) findViewById(R.id.subscribe);

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
                    bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, QuickstartPreferences.convertToDate(birthday.getText().toString()));
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


    private void setDateTimeField() {
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Date", "Click ! ");
                showDatePickerDialog(v);
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("Date", "date results " + year + monthOfYear + dayOfMonth);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthday.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new FragmentDatePickerDialog();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void getDateSpinner() {

    }
}
