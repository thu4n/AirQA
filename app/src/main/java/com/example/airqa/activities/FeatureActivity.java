package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.airqa.R;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        // Handle navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_features);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else {
                return false;
            }
        });
        setInformation();

    }

    private  void setInformation(){
        int count = 1;
        for(WeatherAsset weatherAsset : MapActivity.weatherAssets){
            switch (count){
                case 1:{
                    TextView assetName = findViewById(R.id.assetName1);
                    assetName.setText(weatherAsset.getName());
                    if(weatherAsset.getAttributes().getTemperature() != null && weatherAsset.getAttributes().getTemperature().getValue() > 0){
                        TextView assetTempVal = findViewById(R.id.temp_number1);
                        double tempVal = weatherAsset.getAttributes().getTemperature().getValue();
                        int roundedTempVal = (int) tempVal;
                        assetTempVal.setText(roundedTempVal + "°C");
                    }
                    else{

                    }
                    break;
                }
                case 2:{
                    TextView assetName = findViewById(R.id.assetName2);
                    assetName.setText(weatherAsset.getName());
                    if(weatherAsset.getAttributes().getTemperature() != null && weatherAsset.getAttributes().getTemperature().getValue() > 0){
                        TextView assetTempVal = findViewById(R.id.temp_number2);
                        double tempVal = weatherAsset.getAttributes().getTemperature().getValue();
                        int roundedTempVal = (int) tempVal;
                        assetTempVal.setText(roundedTempVal + "°C");
                    }
                    else{

                    }
                    break;
                }
                case 3:{
                    TextView assetName = findViewById(R.id.assetName3);
                    assetName.setText(weatherAsset.getName());
                    if(weatherAsset.getAttributes().getTemperature() != null && weatherAsset.getAttributes().getTemperature().getValue() > 0){
                        TextView assetTempVal = findViewById(R.id.temp_number3);
                        double tempVal = weatherAsset.getAttributes().getTemperature().getValue();
                        int roundedTempVal = (int) tempVal;
                        assetTempVal.setText(roundedTempVal + "°C");
                    }
                    else{

                    }
                    break;
                }

            }
            count++;
        }
    }
}