package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.DataPoint;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues;

    public String assetId;
    public String assetAtrribute;

    MaterialAutoCompleteTextView inputAssetName,inputAssetType, inputStartDate, inputEndDate;
    MaterialButton materialButton;
    TextInputLayout inputAssetNameLayout,inputAssetTypeLayout, inputStartLayout, inputEndDateLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Handle navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chart);
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

        TextInputLayout inputAssetNameLayout = findViewById(R.id.inputAssetNameLayout);
        inputAssetName = findViewById(R.id.inputAssetName);
        materialButton = findViewById(R.id.showBtn);
        TextInputLayout inputAssetTypeLayout = findViewById(R.id.inputAssetTypeLayout);
        inputAssetType = findViewById(R.id.inputAssetType);
        TextInputLayout inputStartDateLayout = findViewById(R.id.inputStartDateLayout);
        inputStartDate = findViewById(R.id.inputStartDate);
        TextInputLayout inputEndDateLayout = findViewById(R.id.inputEndDateLayout);
        inputEndDate = findViewById(R.id.inputEndDate);
        inputStartDate.setOnClickListener(v -> showDatePicker(inputStartDate));
        inputEndDate.setOnClickListener(v -> showDatePicker(inputEndDate));
        setAssetName();
        setAttributeName();

        inputAssetName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for(WeatherAsset weatherAsset: MapActivity.weatherAssets){
                    if(Objects.equals(weatherAsset.getName(), selectedItem)){
                        Log.d("AssetIDforName",weatherAsset.getId());
                        assetId = weatherAsset.getId();

                    }
                }
            }
        });
        inputAssetType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                assetAtrribute = selectedItem;
                Log.d("attribute", assetAtrribute);
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
                String access_token = sharedPreferences.getString("access_token","");
                try {
                    refreshChart(access_token,assetId,assetAtrribute);
                } catch (ParseException e) {
                    Log.d("chart error",e.toString());
                    throw new RuntimeException(e);
                }
            }
        });
        inputEndDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // DRAW CHART
        lineChart = findViewById(R.id.line_chart);
        lineChart.animateXY(2000, 2000); // Animation duration
        lineChart.invalidate(); // Refreshes the chart

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Show a Toast message with the selected entry's value
                long x = (long) e.getX();
                Date date = new Date(x);
                String format = "dd/MM/yyyy";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
                String dateString = sdf.format(date);
                InfoChartFragment fragment = InfoChartFragment.newInstance(assetAtrribute, dateString, Float.toString(e.getY()));

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_infoChart, fragment)
                        .addToBackStack(null) // Optional: Add fragment to back stack
                        .commit();
                }

            @Override
            public void onNothingSelected() {
                // Do something when nothing is selected (optional)
            }
        });
    }
    private void showDatePicker(MaterialAutoCompleteTextView inputTV) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Handle selected date
            String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
            inputTV.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void setAssetName(){
        List<String> names = new ArrayList<>();
        // info chart
        for(WeatherAsset asset : MapActivity.weatherAssets){
            names.add(asset.getName());
        }
        String[] nameArray = names.toArray(new String[0]);
        inputAssetName.setSimpleItems(nameArray);
    }
    private void setAttributeName(){
        /*List<String> names = new ArrayList<>();
        // info chart
        for(WeatherAsset asset : MapActivity.weatherAssets){
            names.add(asset.g);
        }*/
        String[] nameArray = {"temperature", "humidity", "rainfall", "windspeed"};
        inputAssetType.setSimpleItems(nameArray);
    }
    private void setEndDate(){
        Date currentDate = (Date) Date.from(Instant.now());
        inputEndDate.setText(currentDate.toString());
    }

    private long convertDateString (String dateString) throws ParseException {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        java.util.Date date = sdf.parse(dateString);
        long epochMillis = date.getTime();
        return epochMillis;
    }

    private void drawChart(List<Entry> entries){
        LineDataSet dataSet = new LineDataSet(entries, assetAtrribute);
        dataSet.setDrawIcons(false); // Hide legend icon
        dataSet.setDrawValues(false); // Hide legend text
        dataSet.setValueTextSize(100f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        // set COLOR base on type of chart
        dataSet.setColor(Color.BLUE);
        dataSet.setFillAlpha(255);
        dataSet.setDrawCircles(false);

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(false);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateXY(500, 500); // Animation duration
        lineChart.invalidate();
        Log.d("chartDraw", "ok");
    }

    private void refreshChart(String access_token, String assetId, String attributeName) throws ParseException {
        String endDate = inputEndDate.getText().toString();
        long endEpoch = convertDateString(endDate);
        Log.d("epoch",endEpoch + "");
        String startDate = inputStartDate.getText().toString();
        long startEpoch = convertDateString(startDate);
        String rawJsonQuery = "{\"type\":\"lttb\",\"fromTimestamp\":" + startEpoch + ",\"toTimestamp\":" + endEpoch + ",\"amountOfPoints\":50}";
        RequestBody rawJsonBody = RequestBody.create(MediaType.parse("application/json"), rawJsonQuery);
        Call<List<DataPoint>> call = ApiService.apiService.getDataPoint("Bearer " + access_token,rawJsonBody,assetId, attributeName);
        call.enqueue(new Callback<List<DataPoint>>() {
            @Override
            public void onResponse(Call<List<DataPoint>> call, Response<List<DataPoint>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() <= 0){
                        Toast.makeText(ChartActivity.this, "No data to be shown!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("CallDataPoint", "200 OK");
                    List<Entry> entries = new ArrayList<>();
                    assert response.body() != null;
                    for(DataPoint dataPoint : response.body()){
                        entries.add(new Entry(dataPoint.getX(), dataPoint.getY()));
                    }
                    drawChart(entries);

                }
                else {
                    Toast.makeText(ChartActivity.this, "You do not have permission!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DataPoint>> call, Throwable t) {

            }
        });
    }
}