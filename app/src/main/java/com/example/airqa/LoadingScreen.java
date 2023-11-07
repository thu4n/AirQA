package com.example.airqa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        // Close the Loading activity after 2 seconds
        new Handler().postDelayed(() -> {
            finish();
        }, 1000);
    }
}