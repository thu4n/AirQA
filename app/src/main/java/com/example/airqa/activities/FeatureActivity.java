package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airqa.R;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

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
        FloatingActionButton fabButton = findViewById(R.id.fabBtn);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPredictDialog();
            }
        });

        setInformation();

    }

    private String epochToDate(long epoch){
        Date date = new Date(epoch);
        String format = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }

    private  void setInformation(){
        for(WeatherAsset weatherAsset : MapActivity.weatherAssets){
            if(Objects.equals(weatherAsset.getName(), "Default Weather")){
                Log.d("AssetIDforFeatureList",weatherAsset.getId());
                TextView assetName = findViewById(R.id.assetName1);
                assetName.setText(weatherAsset.getName());
                if(weatherAsset.getAttributes().getTemperature() != null && weatherAsset.getAttributes().getTemperature().getValue() > 0){
                    TextView assetTempVal = findViewById(R.id.temp_number1);
                    double tempVal = weatherAsset.getAttributes().getTemperature().getValue();
                    int roundedTempVal = (int) tempVal;
                    assetTempVal.setText(roundedTempVal + "°C");

                    TextView assetLastUpdate = findViewById(R.id.date1);
                    long epoch = weatherAsset.getAttributes().getTemperature().getTimestamp();
                    String date = epochToDate(epoch);
                    assetLastUpdate.setText(date);

                    LinearLayout asset1 = findViewById(R.id.asset1);
                    asset1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("weatherAsset", weatherAsset);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                }

            }else if ( Objects.equals(weatherAsset.getName(), "DHT11 Asset")) {
                TextView assetName = findViewById(R.id.assetName2);
                assetName.setText(weatherAsset.getName());
                if ( weatherAsset.getAttributes().getTemperature() != null && weatherAsset.getAttributes().getTemperature().getValue() > 0 ) {
                    TextView assetTempVal = findViewById(R.id.temp_number2);
                    double tempVal = weatherAsset.getAttributes().getTemperature().getValue();
                    int roundedTempVal = (int) tempVal;
                    assetTempVal.setText(roundedTempVal + "°C");

                    TextView assetLastUpdate = findViewById(R.id.date2);
                    long epoch = weatherAsset.getAttributes().getTemperature().getTimestamp();
                    String date = epochToDate(epoch);
                    assetLastUpdate.setText(date);

                    LinearLayout asset2 = findViewById(R.id.asset2);
                    asset2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("weatherAsset", weatherAsset);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                }
            } else if ( Objects.equals(weatherAsset.getName(), "Weather Asset")) {
                TextView assetName = findViewById(R.id.assetName3);
                assetName.setText(weatherAsset.getName());
                LinearLayout asset3 = findViewById(R.id.asset3);
                asset3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("ClickLayout3", weatherAsset.getAttributes().getCO2().getValue() + "");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("weatherAsset", weatherAsset);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });
            }
        }
    }
    private void showPredictDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.predictsheetlayout);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
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
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    };
    private void showTextDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.predictsheetlayout);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
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

}