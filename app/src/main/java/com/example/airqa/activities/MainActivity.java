package com.example.airqa.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    public static final String PREFS_NAME = "preferences";
    MyDatabaseHelper dbHelper;
    Button button;
    LinearLayout dynamicContent, bottomNavBar;
    TextView Humidity, Temp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottomNavBar = (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);


        Humidity = (TextView) findViewById(R.id.humidity);
        Temp = (TextView) findViewById(R.id.temp_number);
        //get the reference of RadioGroup.

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.home);

        // Change the corresponding icon and text color on nav button click.

        rb.setTextColor(Color.parseColor("#1E9CE1"));
        rb.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.primary)));

        // get access token
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String check = sharedPreferences.getString("access_token", "");
        // get all asset info
        getAllAsset(check);

        // Lấy dữ liệu từ cơ sở dữ liệu

        dbHelper = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.readAllweatherASSETData();
        StringBuilder data = new StringBuilder();
        StringBuilder data1 = new StringBuilder();
        // Lấy toàn bộ dữ liệu của ASSET
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String idAsset = cursor.getString(cursor.getColumnIndex("idAsset"));
                @SuppressLint("Range") long createdOn = cursor.getLong(cursor.getColumnIndex("CreatedOn"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
                @SuppressLint("Range") int accessPublicRead = cursor.getInt(cursor.getColumnIndex("AccessPublicRead"));
                @SuppressLint("Range") int aqi = cursor.getInt(cursor.getColumnIndex("AQI"));
                @SuppressLint("Range") int co2 = cursor.getInt(cursor.getColumnIndex("CO2"));
                @SuppressLint("Range") int humidity = cursor.getInt(cursor.getColumnIndex("Humidity"));
                @SuppressLint("Range") double locationX = cursor.getDouble(cursor.getColumnIndex("LocationX"));
                @SuppressLint("Range") double locationY = cursor.getDouble(cursor.getColumnIndex("LocationY"));
                @SuppressLint("Range") double pm10 = cursor.getDouble(cursor.getColumnIndex("PM10"));
                @SuppressLint("Range") double pm25 = cursor.getDouble(cursor.getColumnIndex("PM25"));
                @SuppressLint("Range") double rainfall = cursor.getDouble(cursor.getColumnIndex("Rainfall"));
                @SuppressLint("Range") double temperature = cursor.getDouble(cursor.getColumnIndex("Temperature"));
                @SuppressLint("Range") long timestamp = cursor.getLong(cursor.getColumnIndex("Timestamp"));

                // Construct the data string or perform any desired operations
                String data2 = "ID Asset: " + idAsset +
                        "\nCreated On: " + createdOn +
                        "\nName: " + name +
                        "\nAccess Public Read: " + accessPublicRead +
                        "\nAQI: " + aqi +
                        "\nCO2: " + co2 +
                        "\nHumidity: " + humidity +
                        "\nLocationX: " + locationX +
                        "\nLocationY: " + locationY +
                        "\nPM10: " + pm10 +
                        "\nPM25: " + pm25 +
                        "\nRainfall: " + rainfall +
                        "\nTemperature: " + temperature +
                        "\nTimestamp: " + timestamp;

                // Sử dụng giá trị cuối cùng ở đây...
                data1.append("ID: ").append(id).append(" Rainfall: ").append(rainfall).append(", Temperature: ").append(temperature).append("\n");
                //
                Humidity.setText(data1);
                // Log.e("ok1",data2.toString());
            } while (cursor.moveToNext());
        }

        // Lấy giá trị cuối cùng trong database (giá trị gần nhất được thêm vào)
        if (cursor.moveToLast()) {
            //     @SuppressLint("Range") String rainfall = cursor.getString(cursor.getColumnIndex("RAINFALL"));
            //     @SuppressLint("Range") String temperature = cursor.getString(cursor.getColumnIndex("TEMPERATURE"));
            //     @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            Temp.setText(String.valueOf(id));
            // Sử dụng giá trị cuối cùng ở đây...
            //     data1.append("ID: ").append(ID).append(" Rainfall: ").append(rainfall).append(", Temperature: ").append(temperature).append("\n");
        }
        cursor.close();
        // Lấy ngày
        Calendar calendar = Calendar.getInstance();
        //test.setText( calendar.getTime().toString());
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

        //dbHelper.insertAssetData(sampleRainfall, sampleTemperature, sampleHumidity,
              //  sampleWindSpeed, sampleWindDirection, samplePlace,sampleDaytime);

        //Toast.makeText(MainActivity.this, "Data inserted!", Toast.LENGTH_SHORT).show();
    }
    //get data in asset
    public  void getAssetInfo(String access_token, String id) {
        Call<WeatherAsset> call = ApiService.apiService.getAssetInfo("Bearer " + access_token, id);
        Log.e("ok2", access_token + "");
        call.enqueue(new Callback<WeatherAsset>() {
            @Override
            public void onResponse(Call<WeatherAsset> call, Response<WeatherAsset> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null; // make sure the body isn't null
                    // int humid = response.body().getAttributes().getHumidity().getValue();
                    //Humidity.setText(String.valueOf(humid));
                    // double temp = response.body().getAttributes().getTemperature().getValue();
                    // Temp.setText(String.valueOf(temp));
                    // get data and save into the database
                    String idAsset = response.body().getId();
                    long createdOn = response.body().getCreatedOn();
                    String name = response.body().getName();
                    // change boolean to int
                    boolean b = response.body().isAccessPublicRead();
                    int accessPublicRead = b ? 1 : 0;
                    int aqi = -1; // Giá trị mặc định
                    int co2 = -1; // Giá trị mặc định
                    int humidity = -1; // Giá trị mặc định
                    double pm10 = -1.0; // Giá trị mặc định
                    double pm25 = -1.0; // Giá trị mặc định
                    double rainfall = -1.0; // Giá trị mặc định
                    double temperature = -1.0; // Giá trị mặc định
                    long timestamp = -1; // Giá trị mặc định

                    // Kiểm tra và gán giá trị từ response
                    if (response.body().getAttributes().getAqi() != null) {
                        aqi = response.body().getAttributes().getAqi().getValue();
                    }

                    if (response.body().getAttributes().getCo2() != null) {
                        co2 = response.body().getAttributes().getCo2().getValue();
                    }

                    if (response.body().getAttributes().getHumidity() != null) {
                        humidity = response.body().getAttributes().getHumidity().getValue();
                        timestamp = response.body().getAttributes().getHumidity().getTimestamp();
                    }

                    if (response.body().getAttributes().getPm10() != null) {
                        pm10 = response.body().getAttributes().getPm10().getValue();
                    }

                    if (response.body().getAttributes().getPm25() != null) {
                        pm25 = response.body().getAttributes().getPm25().getValue();
                    }

                    if (response.body().getAttributes().getRainfall() != null) {
                        rainfall = response.body().getAttributes().getRainfall().getValue();
                    }

                    if (response.body().getAttributes().getTemperature() != null) {
                        temperature = response.body().getAttributes().getTemperature().getValue();
                    }

                    List<String> assetIds = new ArrayList<>();
                    double locationX = 10.9;
                    double locationY = 107.0;
                    if (response.body().getAttributes().getLocation().getValue() != null) {
                        List<Double> location = response.body().getAttributes().getLocation().getValue().getCoordinates();
                        locationX = location.get(0);
                        locationY = location.get(1);
                    }

                    dbHelper.insertWeatherAssetData(idAsset, createdOn, name, accessPublicRead, aqi, co2, humidity, locationX, locationY, pm10, pm25, rainfall, temperature, timestamp);


                } else {

                }
            }

            @Override
            public void onFailure(Call<WeatherAsset> call, Throwable t) {

                Log.e("ok1", t.toString());
            }
        });
    }
    public void getAllAsset(String access_token){
        // Default value for the body, do not change this query
        String rawJsonQuery = "{\n" +
                "    \"realm\": {\n" +
                "        \"name\": \"master\"\n" +
                "    },\n" +
                "    \"select\": {\n" +
                "        \"attributes\": [\n" +
                "            \"location\",\n" +
                "            \"direction\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"attributes\": {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"name\": {\n" +
                "                    \"predicateType\": \"string\",\n" +
                "                    \"value\": \"location\"\n" +
                "                },\n" +
                "                \"meta\": [\n" +
                "                    {\n" +
                "                        \"name\": {\n" +
                "                            \"predicateType\": \"string\",\n" +
                "                            \"value\": \"showOnDashboard\"\n" +
                "                        },\n" +
                "                        \"negated\": true\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": {\n" +
                "                            \"predicateType\": \"string\",\n" +
                "                            \"value\": \"showOnDashboard\"\n" +
                "                        },\n" +
                "                        \"value\": {\n" +
                "                            \"predicateType\": \"boolean\",\n" +
                "                            \"value\": true\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        RequestBody rawJsonBody = RequestBody.create(MediaType.parse("application/json"), rawJsonQuery);
        Call<List<Asset>> call = ApiService.apiService.getAllAsset("Bearer " + access_token, rawJsonBody);

        call.enqueue(new Callback<List<Asset>>() {
            @Override
            public void onResponse(Call<List<Asset>> call, Response<List<Asset>> response) {
                if(response.isSuccessful()){
                    List<Asset> assets = response.body();
                    List<String> assetIds = new ArrayList<>();
                    for(Asset asset : assets){
                        if(asset.getType().equals("WeatherAsset")){ // WeatherAsset is the one that contains temperature,humidity...
                            String id = asset.getId();
                            assetIds.add(id);
                            Log.e("ok",id);
                        }
                    }
                    getAssetInfo(access_token, assetIds.get(0));

                }
                else {
                    Toast.makeText(MainActivity.this, "You do not have permission!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Asset>> call, Throwable t) {
                Log.e("failedCall",t.toString());
            }
        });
    }
}