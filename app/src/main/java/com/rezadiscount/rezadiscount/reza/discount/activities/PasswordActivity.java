package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonListener;
import com.rezadiscount.rezadiscount.reza.discount.utilities.GetJsonResult;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PasswordActivity extends Activity implements GetJsonListener {

    private EditText emailText;
    private Button sendEmailButton;
    private View.OnClickListener sendEmail;
    private GetJsonResult jsonResult;
    private GetJsonListener jsonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setValuesId();
    }

    private void setValuesId() {
        emailText = (EditText) findViewById(R.id.mail_forgotten);
        sendEmailButton = (Button) findViewById(R.id.send_email);

        sendEmail = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if email field is empty
                if (emailText.getText().toString().equals("")) {
                    emailText.setError(PasswordActivity.this.getResources().getString(R.string.email_empty));

                } else if (!QuickstartPreferences.isValidEmail(emailText.getText().toString())) {
                    emailText.setError(PasswordActivity.this.getResources().getString(R.string.email_incorrect));
                } else { // Email is valid
                    sendEmailHttp();
                }

            }
        };

        sendEmailButton.setOnClickListener(sendEmail);
    }

    protected void sendEmailHttp() {
        HashMap<String, String> headerList = new HashMap<>();


        // TODO Remove Lat and Long from this query
        headerList.put(QuickstartPreferences.TAG_LATITUDE, "1337");
        headerList.put(QuickstartPreferences.TAG_LONGITUDE, "1337");

        JSONObject bodyAuth = new JSONObject();
        JSONObject parent = new JSONObject();

        try {
            bodyAuth.put(QuickstartPreferences.TAG_EMAIL, emailText.getText().toString());
            parent.put(QuickstartPreferences.TAG_PASSWORD_RECOVER, bodyAuth);
            Log.d("email", emailText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        jsonResult = new GetJsonResult();
        jsonResult.setParams(this, headerList, QuickstartPreferences.URL_FORPSSWD, QuickstartPreferences.TAG_POST, parent);
        jsonResult.addListener(jsonListener);
        jsonResult.execute();
    }


    @Override
    public void getJsonObject() {

        // if Json return isn't null
        if (jsonResult != null) {

            String code_retour = "";
            String message_retour = "";
            // If subscription
            try {
                code_retour = jsonResult.getJson().getString(QuickstartPreferences.TAG_HTTPCODE);
                message_retour = jsonResult.getJson().getString(QuickstartPreferences.TAG_MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Message", "Message : " + message_retour);

            // Si tout s'est bien passé
            if (code_retour.equals("200")) {
                Toast.makeText(this, this.getResources().getString(R.string.password_email_success), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show();
            }
        }
    }
}
