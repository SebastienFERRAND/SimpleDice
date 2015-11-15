package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.rezadiscount.rezadiscount.R;

public class SignInUp extends AppCompatActivity {

    private Button connexion;
    private Button inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        connexion = (Button) findViewById(R.id.connexion);
        inscription = (Button) findViewById(R.id.inscription);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(SignInUp.this, LoginActivity.class);
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
