package com.example.airqa.activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airqa.R;
import com.example.airqa.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Handle navbar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(SettingsActivity.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                startActivity(new Intent(SettingsActivity.this, FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(SettingsActivity.this, ChartActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                return true;
            } else {
                return false;
            }
        });
        FloatingActionButton fabButton = findViewById(R.id.fabBtn); // Replace R.id.fabButton with your FAB ID

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPredictDialog();
            }
        });


        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // below line is to inflate our fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.idFrameLayout, new SettingsFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }

    }
    private void showPredictDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.predictsheetlayout);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        TextView tempVal = dialog.findViewById(R.id.assetIdTempValue1);
        TextView humidVal = dialog.findViewById(R.id.assetIdHumidValue1);
        TextView windSpeedVal = dialog.findViewById(R.id.assetIdWindSpeedValue1);
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String T = sharedPreferences.getString("PredTemperature", "");
        String H = sharedPreferences.getString("PredHumidity", "");
        String W = sharedPreferences.getString("PredWindSpeed", "");


        tempVal.setText( String.valueOf(T));
        humidVal.setText(String.valueOf(H));
        windSpeedVal.setText(String.valueOf(W));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Log.d("click predict", "show dialog");
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Material3_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
    };
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
    }
}
