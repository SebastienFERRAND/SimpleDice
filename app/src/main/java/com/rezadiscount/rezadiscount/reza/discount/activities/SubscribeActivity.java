package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;
import com.rezadiscount.rezadiscount.reza.discount.utilities.SharedPreferencesModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.util.HashMap;

public class SubscribeActivity extends AppCompatActivity implements GetJsonListener {


    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private EditText password;
    private EditText birthday;
    private EditText gender;


    private Button subscribe;

    private GetJsonResult jsonResult;
    private GetJsonListener jsonListener;

    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        act = this;
        jsonListener = this;

        lastName = (EditText) findViewById(R.id.lastname);
        firstName = (EditText) findViewById(R.id.firstname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        birthday = (EditText) findViewById(R.id.birthday);
        gender = (EditText) findViewById(R.id.gender);

        subscribe = (Button) findViewById(R.id.subscribe);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> headerList = new HashMap<String, String>();
                headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
                headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");

                JSONObject bodyAuth = new JSONObject();
                JSONObject parent=new JSONObject();

                try {
                    bodyAuth.put(QuickstartPreferences.TAG_LASTNAME, lastName.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_FIRSTNAME, firstName.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_EMAIL, email.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_PASSWD, password.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_BIRTHDAY, birthday.getText().toString());
                    bodyAuth.put(QuickstartPreferences.TAG_GENDER, gender.getText().toString());
                    parent.put("register", bodyAuth);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonResult = new GetJsonResult();
                jsonResult.setParams(act, headerList, QuickstartPreferences.url_reg, "POST", parent);
                jsonResult.addListener(jsonListener);
                jsonResult.execute();
            }
        });


    }

    @Override
    public void getJsonObject() {

        Log.d("Test", jsonResult.getJson().toString());

    }
}
