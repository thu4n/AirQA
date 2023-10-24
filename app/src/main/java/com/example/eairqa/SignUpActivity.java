package com.example.eairqa;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    // define the global variable
    TextView change_to_login_button;
    // Add button Move previous activity
    MaterialButton signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // by ID we can use each component which id is assign in xml
        // file use findViewById() to get the Button and textview.
        signup_button = (MaterialButton) findViewById(R.id.signup_button);
        change_to_login_button = (TextView) findViewById(R.id.change_to_login);

        // Change to login button add click-listener
        change_to_login_button.setOnClickListener(v -> {
            // Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining
            // the components you are targeting. Intent to start an activity called SecondActivity with the following code:
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            // start the activity connect to the specified class
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
