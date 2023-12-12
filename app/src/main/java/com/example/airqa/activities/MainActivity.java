package com.example.airqa.activities;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.airqa.fragments.AttributeAQIContainerFragment;
import com.example.airqa.fragments.AttributeContainerFragment;
import com.example.airqa.fragments.AttributePolluContainerFragment;
import com.example.airqa.models.weatherAssetGroup.Humidity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.database.Cursor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.airqa.MyDatabaseHelper;
import com.example.airqa.R;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;

public class MainActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper;

    BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set language
        SharedPreferences sharedPreferences1 = getSharedPreferences("preferences", MODE_PRIVATE);
        String savedLanguage = sharedPreferences1.getString("language", "");
        setLocale(MainActivity.this,savedLanguage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Handle navbar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                startActivity(new Intent(MainActivity.this, FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

        //Receive weather asset from the map activity
        WeatherAsset weatherAsset = getIntent().getParcelableExtra("weatherAsset");
        setInformation(weatherAsset);

        // loading database
        // Lấy dữ liệu từ cơ sở dữ liệu
        dbHelper = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.readAllASSETData();
        StringBuilder data = new StringBuilder();
        StringBuilder data1 = new StringBuilder();
        // Lấy toàn bộ dữ liệu của ASSET
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String rainfall = cursor.getString(cursor.getColumnIndex("RAINFALL"));
                @SuppressLint("Range") String temperature = cursor.getString(cursor.getColumnIndex("TEMPERATURE"));
                @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex("ID"));

                // Lấy thông tin từ các cột khác...

                // Thêm dữ liệu vào StringBuilder
                data.append("ID: ").append(ID).append("Rainfall: ").append(rainfall).append(", Temperature: ").append(temperature).append("\n");
                // Lấy giá trị cuôối cùng

            } while (cursor.moveToNext());
        }
        // Lấy giá trị cuối cùng trong database (giá trị gần nhất được thêm vào)
        if (cursor.moveToLast()) {
            @SuppressLint("Range") String rainfall = cursor.getString(cursor.getColumnIndex("RAINFALL"));
            @SuppressLint("Range") String temperature = cursor.getString(cursor.getColumnIndex("TEMPERATURE"));
            @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex("ID"));

            // Sử dụng giá trị cuối cùng ở đây...
            data1.append("ID: ").append(ID).append(" Rainfall: ").append(rainfall).append(", Temperature: ").append(temperature).append("\n");
        }
        cursor.close();

    }

    private void insertSampleData() {
        String sampleRainfall = "11";
        String sampleTemperature = "65";
        String sampleHumidity = "70";
        String sampleWindSpeed = "10";
        String sampleWindDirection = "North";
        String samplePlace = "Sample Place";

        Calendar calendar = Calendar.getInstance();
        String sampleDaytime = calendar.getTime().toString();

        dbHelper.insertAssetData(sampleRainfall, sampleTemperature, sampleHumidity,
                sampleWindSpeed, sampleWindDirection, samplePlace,sampleDaytime);

        //Toast.makeText(MainActivity.this, "Data inserted!", Toast.LENGTH_SHORT).show();
    }

    private void setHumidityFragment(String value, String description, String avgValue){
        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.humid_icon);
        String title = getResources().getString(R.string.Humidity);
        String unit = "%";
        AttributeContainerFragment fragment = AttributeContainerFragment.newInstance(
                icon,
                title,
                value,
                unit,
                description,
                avgValue + unit
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_humid, fragment)
                .commit();
    }

    private void setWindSpeedFragment(String value, String description, String avgValue){
        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.baseline_wind_power_24);
        String title = getResources().getString(R.string.WindSpeed);
        String unit = " km/h";
        AttributeContainerFragment fragment = AttributeContainerFragment.newInstance(
                icon,
                title,
                value,
                unit,
                description,
                avgValue + unit
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_windspeed, fragment)
                .commit();
    }

    private void setRainfallFragment(String value, String description, String avgValue){
        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.rainfall);
        String title = getResources().getString(R.string.Rainfall);
        String unit = " mm";
        AttributeContainerFragment fragment = AttributeContainerFragment.newInstance(
                icon,
                title,
                value,
                unit,
                description,
                avgValue + unit
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_rainfall, fragment)
                .commit();
    }
    private void setPollutantFragment(String pm10, String pm25, String co2){
        String unit1 = " µg/m3";
        String unit2 = " µg/m3";
        String unit3 = " ppm";
        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.pollu_icon);
        AttributePolluContainerFragment fragment = AttributePolluContainerFragment.newInstance(
                icon,
                "Pollutants",
                "PM10",
                pm10,
                unit1,
                "PM25",
                pm25,
                unit2,
                "CO2",
                co2,
                unit3
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_pollutant, fragment)
                .commit();
    }

    private void setAqiFragment(String value){
        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.logo);
        int intVal = Integer.parseInt(value);
        int percent = (intVal * 100) / 100 ;
        AttributeAQIContainerFragment fragment = AttributeAQIContainerFragment.newInstance(
                icon,
                "AQI",
                value,
                percent
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_aqi, fragment)
                .commit();
    }

    private String getRoundedString(double value){
        String content;
        if(value < 1)
        {
            content = value + "";
            return content;
        }
        else
        {
            int roundedValue = (int) value;
            content = roundedValue +  "";
            return  content;
        }
    }

    private String epochToDate(long epoch){
        Date date = new Date(epoch);
        String format = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }
    private void setInformation(WeatherAsset asset){
        TextView temperature, humidity, rainfall, windspeed,assetName,assetId, timestamp;

        assetName = findViewById(R.id.assetNameInfo);
        assetName.setText(asset.getName());
        assetId = findViewById(R.id.assetIdInfo);
        assetId.setText(asset.getId());
        String tempString = "";
        // Set the date for the most recent update
        if(asset.getAttributes().getTemperature() != null){
            timestamp = findViewById(R.id.timestampVal);
            long timestampVal = asset.getAttributes().getTemperature().getTimestamp();
            Log.d("epochTimestamp",timestampVal + "");
            String lastUpdated = epochToDate(timestampVal);
            timestamp.setText(lastUpdated);
            temperature = findViewById(R.id.temp_number);
            double tempValue = asset.getAttributes().getTemperature().getValue();
            String temp = getRoundedString(tempValue);
            temperature.setText(temp);
            tempString = temp;
        }

        // Only the one on the map has this value as True
        if(asset.isAccessPublicRead()){

            double humidValue = asset.getAttributes().getHumidity().getValue();
            double rainfallValue = asset.getAttributes().getRainfall().getValue();
            double windspeedValue = asset.getAttributes().getWindSpeed().getValue();

            String humidString = getRoundedString(humidValue);
            String rainfallString = getRoundedString(rainfallValue);
            String windspeedString = getRoundedString(windspeedValue);

             // Script text info weather
            TextView textView = findViewById(R.id.textInformation);
            textView.setText("");
            LocalTime currentTime = LocalTime.now();
            int hour = currentTime.getHour();
            String weatherText ="";
            if (hour >= 5 && hour < 11) {
                weatherText = getString(R.string.morning_weather, tempString, humidString, windspeedString, rainfallString);
            } else if (hour >= 11 && hour < 15) {
                weatherText = getString(R.string.noon_weather, tempString, humidString, windspeedString, rainfallString);
            } else if (hour >= 15 && hour < 18) {
                weatherText = getString(R.string.afternoon_weather, tempString, humidString, windspeedString, rainfallString);
            } else if (hour >= 18 && hour < 22) {
                weatherText = getString(R.string.evening_weather, tempString, humidString, windspeedString, rainfallString);
            } else if (hour >= 22 || hour < 5) {
                weatherText = getString(R.string.night_weather, tempString, humidString, windspeedString, rainfallString);
            } else {
                weatherText = getString(R.string.dawn_weather, tempString, humidString, windspeedString, rainfallString);
            }

            textView.setText(weatherText);

            setHumidityFragment(humidString,getResources().getString(R.string.Actual_value), humidValue + "");
            setRainfallFragment(rainfallString, getResources().getString(R.string.Actual_value), rainfallValue + "");
            setWindSpeedFragment(windspeedString, getResources().getString(R.string.Actual_value), windspeedValue + "");
            return;
        } else if (asset.getAttributes().getPM10() != null) {
            temperature = findViewById(R.id.temp_number);
            temperature.setText("N/A");
            double pm10Value = asset.getAttributes().getPM10().getValue();
            double pm25Value = asset.getAttributes().getPM25().getValue();
            double co2Value = asset.getAttributes().getCO2().getValue();
            Integer aqi = asset.getAttributes().getAQI().getValue();

            String pm10String = pm10Value + "";
            String pm25String = pm25Value + "";
            String co2String = co2Value + "";
            String aqiString = aqi + "";
            // set script
            TextView textView = findViewById(R.id.textInformation);
            textView.setText("");
            LocalTime currentTime = LocalTime.now();
            int hour = currentTime.getHour();
            String weatherText ="";
            if (hour >= 5 && hour < 11) {
                weatherText = getString(R.string.morning_weather2, pm10String, pm25String, co2String, aqiString);
            } else if (hour >= 11 && hour < 15) {
                weatherText = getString(R.string.noon_weather2, pm10String, pm25String,co2String,aqiString);
            } else if (hour >= 15 && hour < 18) {
                weatherText = getString(R.string.afternoon_weather2, pm10String, pm25String,co2String,aqiString);
            } else if (hour >= 18 && hour < 22) {
                weatherText = getString(R.string.evening_weather2, pm10String, pm25String,co2String,aqiString);
            } else if (hour >= 22 || hour < 5) {
                weatherText = getString(R.string.night_weather2, pm10String, pm25String,co2String,aqiString);
            } else {
                weatherText = getString(R.string.dawn_weather2, pm10String, pm25String,co2String,aqiString);
            }

            textView.setText(weatherText);
            setPollutantFragment(pm10String, pm25String, co2String);
            setAqiFragment(aqiString);
        }
        else{
            double humidValue = asset.getAttributes().getHumidity().getValue();
            String humidString = getRoundedString(humidValue);
            setHumidityFragment(humidString,getResources().getString(R.string.Actual_value), humidValue + "");
            TextView textView = findViewById(R.id.textInformation);
            textView.setText("");
            LocalTime currentTime = LocalTime.now();
            int hour = currentTime.getHour();
            String weatherText ="";
            if (hour >= 5 && hour < 11) {
                weatherText = getString(R.string.good_morning1, tempString, humidString);
            } else if (hour >= 11 && hour < 15) {
                weatherText = getString(R.string.hello_noon1, tempString, humidString);
            } else if (hour >= 15 && hour < 18) {
                weatherText = getString(R.string.good_afternoon1, tempString, humidString);
            } else if (hour >= 18 && hour < 22) {
                weatherText = getString(R.string.good_evening1, tempString, humidString);
            } else if (hour >= 22 || hour < 5) {
                weatherText = getString(R.string.good_night1, tempString, humidString);
            } else {
                weatherText = getString(R.string.good_dawn1, tempString, humidString);
            }
            textView.setText(weatherText);
            return;
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
    private void setLocale(MainActivity activity, String languages){
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}