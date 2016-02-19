package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.HTTPObjects.HTTPStandardReturn;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonListenerPassword;
import com.rezadiscount.rezadiscount.reza.discount.WebServices.GetJsonResultPassword;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import org.json.JSONObject;

public class PasswordActivity extends Activity implements GetJsonListenerPassword {

    private EditText emailText;
    private Button sendEmailButton;
    private View.OnClickListener sendEmail;
    private GetJsonResultPassword jsonResultPassword;
    private GetJsonListenerPassword jsonListenerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setValuesId();
    }

    private void setValuesId() {
        jsonListenerPassword = this;
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


        JSONObject bodyAuth = new JSONObject();
        JSONObject parent = new JSONObject();

        try {
            bodyAuth.put(QuickstartPreferences.TAG_EMAIL, emailText.getText().toString());
            parent.put(QuickstartPreferences.TAG_PASSWORD_RECOVER, bodyAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonResultPassword = new GetJsonResultPassword();
        jsonResultPassword.setParams(this, parent);
        jsonResultPassword.addListener(jsonListenerPassword);
        jsonResultPassword.execute();
    }

    @Override
    public void getReturnPassword() {

        HTTPStandardReturn passwordReturn = jsonResultPassword.getReturnPassword();

        // Si tout s'est bien passé
        if (passwordReturn.getCode().equals("200")) {

            // Start Home activity
            Intent myIntent = new Intent(PasswordActivity.this, SignInUp.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            myIntent.putExtra("fromRecovery", true);
            PasswordActivity.this.startActivity(myIntent);

        } else {
            //TODO set different error messages depending on return
            emailText.setError("Erreur");
        }

    }
}
