package com.example.airqa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    // define the global variable
    TextView change_to_signup_button;
    MaterialButton login_button;
    // Add button Move to next Activity and previous Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // by ID we can use each component which id is assign in xml
        // file use findViewById() to get the both Button and textview
        MaterialButton login_button = (MaterialButton) findViewById(R.id.login_button);
        TextView change_to_signup_button = (TextView) findViewById(R.id.change_to_signup);

        // Change to sign up button add click-listener
        change_to_signup_button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            // start the activity connect to the specified class
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        login_button.setOnClickListener(v -> {

            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // start the activity connect to the specified class
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
        });
    }
    private void logIn(){

    }


}
