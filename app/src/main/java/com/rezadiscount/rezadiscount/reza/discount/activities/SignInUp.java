package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rezadiscount.rezadiscount.R;


import com.rezadiscount.rezadiscount.reza.discount.utilities.JsonParserLogin;

import org.json.JSONObject;

public class SignInUp extends AppCompatActivity {

    private Button connexion;
    private Button inscription;

    private EditText connexionField;
    private EditText passwordField;

    private static String url_merc = "auth";
    private static final String TAG_RESULT = "results";
    private static final String TAG_TOKEN = "token";
    private JSONObject android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        connexion = (Button) findViewById(R.id.connexion);
        inscription = (Button) findViewById(R.id.inscription);

        connexionField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.passwd_field);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JsonParserLogin().execute(connexionField.getText().toString(), passwordField.getText().toString(), url_merc);

                Intent myIntent = new Intent(SignInUp.this, BusinessFilterResearch.class);
                SignInUp.this.startActivity(myIntent);

            }
        });

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInUp.this, SubscribeActivity.class);
                SignInUp.this.startActivity(myIntent);
            }
        });

    }

}
