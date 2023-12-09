package com.example.airqa.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.airqa.models.weatherAssetGroup.Humidity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.airqa.MyDatabaseHelper;
import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "preferences";
    MyDatabaseHelper dbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Handle navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                startActivity(new Intent(getApplicationContext(), FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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

        //Receive weather asset from the map activity
        WeatherAsset weatherAsset = getIntent().getParcelableExtra("weatherAsset");
//        setInformation(weatherAsset,0);

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

        // Lấy ngày
        Calendar calendar = Calendar.getInstance();
        //test.setText( calendar.getTime().toString());


        Drawable icon = ContextCompat.getDrawable(getBaseContext(), R.drawable.humid_icon);
        String title = "Title";
        String value = "123";
        String unit = "kg";
        String description = "Description";
        String avgValue = "100";
        CustomStatContainerFragment fragment = CustomStatContainerFragment.newInstance(
                icon,
                "Title",
                "123",
                "kg",
                "Description",
                "100"
        );
        CustomStatContainerFragment2 fragment2 = CustomStatContainerFragment2.newInstance(
                icon,
                "Title",
                "123",
                "123",
                "123",
                "123",
                "123",
                "123",
                "123",
                "123",
                "123"
        );
        CustomStatContainerFragment3 fragment3 = CustomStatContainerFragment3.newInstance(
                icon,
                "Title",
                "123"
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_aqi, fragment3)
                .commit();
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

    private void setTextViewValue(TextView textView, double value, String unit){
        String content;
        if(value < 1)
        {
            content = value +  unit;
        }
        else
        {
            int roundedValue = (int) value;
            content = roundedValue +  unit;
        }
        textView.setText(content);
    }

//    private void setInformation(WeatherAsset weatherAsset, int id){
//        TextView temperature, humidity, rainfall, windspeed,assetName,assetId;
//        assetName = (TextView) findViewById(R.id.assetNameInfo);
//        assetId = (TextView) findViewById(R.id.assetIdInfo);
////        humidity = (TextView) findViewById(R.id.humidityVal);
//        temperature = (TextView) findViewById(R.id.temp_number);
//        rainfall = (TextView) findViewById(R.id.rainfallValue);
//        windspeed = (TextView) findViewById(R.id.windspeedVal);
//        TextView timestamp = findViewById(R.id.timestampVal);
//
//        LinearLayout aqiRow = findViewById(R.id.aqiRow);
//        LinearLayout pollutantRow = findViewById(R.id.pollutantRow);
////        LinearLayout humidRow = findViewById(R.id.humidityRow);
//        LinearLayout rainfallRow = findViewById(R.id.rainfallRow);
//        LinearLayout windspeedRow = findViewById(R.id.windspeedRow);
//
//        switch (id){
//            case 0:{
//                aqiRow.setVisibility(View.INVISIBLE);
//                pollutantRow.setVisibility(View.INVISIBLE);
//
//                double tempValue = weatherAsset.getAttributes().getTemperature().getValue();
//                double humidValue = weatherAsset.getAttributes().getHumidity().getValue();
//                double rainfallValue = weatherAsset.getAttributes().getRainfall().getValue();
//                double windspeedValue = weatherAsset.getAttributes().getWindSpeed().getValue();
//                long timestampVal = weatherAsset.getAttributes().getHumidity().getTimestamp();
//                Instant instant = Instant.ofEpochMilli(timestampVal);
//                Log.d("timestamp",timestampVal + "");
//
//                setTextViewValue(temperature, tempValue , "°C");
////                setTextViewValue(humidity,humidValue, "%");
//                setTextViewValue(rainfall,rainfallValue, "mm");
//                setTextViewValue(windspeed,windspeedValue, "km/h");
//                assetName.setText(weatherAsset.getName());
//                timestamp.setText(instant.toString());
//                Log.d("idE", weatherAsset.getId());
//                assetId.setText(weatherAsset.getId());
//                break;
//            }
//            case 1:
//            {
//
//            }
//        }
//    }
}