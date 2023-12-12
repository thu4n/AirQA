package com.example.airqa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.fragments.InfoChartFragment;
import com.example.airqa.models.DataPoint;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
public class ChartActivity extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues;
    private List<String[]> data;
    public String assetId;
    public String assetAtrribute;
    BottomNavigationView bottomNavigationView;
    private static final int CREATE_FILE_REQUEST_CODE = 1;
    MaterialAutoCompleteTextView inputAssetName,inputAssetType, inputStartDate, inputEndDate;
    MaterialButton materialButton,Downloadbtn;
    TextInputLayout inputAssetNameLayout,inputAssetTypeLayout, inputStartLayout, inputEndDateLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        data = new ArrayList<>();
        data.add(new String[]{"AsserID", "Attribute","Value","Timestamp"});
        // Handle navbar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(ChartActivity.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                startActivity(new Intent(ChartActivity.this, FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(ChartActivity.this, SettingsActivity.class));
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

        TextInputLayout inputAssetNameLayout = findViewById(R.id.inputAssetNameLayout);
        inputAssetName = findViewById(R.id.inputAssetName);
        materialButton = findViewById(R.id.showBtn);
        Downloadbtn = findViewById(R.id.downloadBtn);
        TextInputLayout inputAssetTypeLayout = findViewById(R.id.inputAssetTypeLayout);
        inputAssetType = findViewById(R.id.inputAssetType);
        TextInputLayout inputStartDateLayout = findViewById(R.id.inputStartDateLayout);
        inputStartDate = findViewById(R.id.inputStartDate);
        TextInputLayout inputEndDateLayout = findViewById(R.id.inputEndDateLayout);
        inputEndDate = findViewById(R.id.inputEndDate);

        inputStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(inputStartDate);
            }
        });
        inputEndDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(inputEndDate);
            }
        });

        
        setAssetName();
        setAttributeName();

        inputAssetName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for(WeatherAsset weatherAsset: SplashScreen.weatherAssets){
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
                String format = "dd/MM/yyyy HH:mm";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
                String dateString = sdf.format(date);
                InfoChartFragment fragment = InfoChartFragment.newInstance(assetAtrribute, Float.toString(e.getY()) ,dateString);

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
        Downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
                String access_token = sharedPreferences.getString("access_token","");
                createFile();

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
        for(WeatherAsset asset : SplashScreen.weatherAssets){
            names.add(asset.getName());
        }
        String[] nameArray = names.toArray(new String[0]);
        inputAssetName.setSimpleItems(nameArray);
    }
    private void setAttributeName(){
        String[] nameArray = {"temperature", "humidity", "rainfall", "windspeed", "PM10", "PM25", "CO2"};
        inputAssetType.setSimpleItems(nameArray);
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
        data.clear();
        data.add(new String[]{"AsserID", "Attribute","Value","Timestamp"});
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
                    if( response.body().size() == 0 ){
                        data.clear();
                        lineChart.clear();
                        Toast.makeText(ChartActivity.this, "No data to be shown!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("CallDataPoint", "200 OK");
                    List<Entry> entries = new ArrayList<>();
                    assert response.body() != null;
                    for(DataPoint dataPoint : response.body()){
                        entries.add(new Entry(dataPoint.getX(), dataPoint.getY()));
                        data.add(new String[]{assetId,attributeName, String.valueOf(dataPoint.getY()),String.valueOf(dataPoint.getX())});
                    }
                    drawChart(entries);

                }
                else {
                    lineChart.clear();
                    Toast.makeText(ChartActivity.this, "Error, make sure that the asset do have this attribute", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DataPoint>> call, Throwable t) {
            }
        });
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
    private void saveDataAsCSV(List<String[]> data, Uri uri) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContentResolver().openOutputStream(uri));
            CSVWriter csvWriter = new CSVWriter(outputStreamWriter);
            csvWriter.writeAll(data);
            csvWriter.close();
            Toast.makeText(this, "Data saved as CSV file!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving CSV file!", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveAsCSV(Uri uri) {
        Uri fileUri = uri;
        saveDataAsCSV(data, fileUri);
    }
    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "text");
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    SaveAsCSV(uri);

                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.bottom_chart);
    }
}