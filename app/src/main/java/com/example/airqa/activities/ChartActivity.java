package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.airqa.R;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues;

    MaterialAutoCompleteTextView inputAssetName,inputAssetType, inputStartDate, inputEndDate;
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
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                startActivity(new Intent(getApplicationContext(), FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
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

        // info chart

        TextInputLayout inputAssetNameLayout = findViewById(R.id.inputAssetNameLayout);
        MaterialAutoCompleteTextView inputAssetName = findViewById(R.id.inputAssetName);
        TextInputLayout inputAssetTypeLayout = findViewById(R.id.inputAssetTypeLayout);
        MaterialAutoCompleteTextView inputAssetType = findViewById(R.id.inputAssetType);
        TextInputLayout inputStartDateLayout = findViewById(R.id.inputStartDateLayout);
        MaterialAutoCompleteTextView inputStartDate = findViewById(R.id.inputStartDate);
        TextInputLayout inputEndDateLayout = findViewById(R.id.inputEndDateLayout);
        MaterialAutoCompleteTextView inputEndDate = findViewById(R.id.inputEndDate);
        inputStartDate.setOnClickListener(v -> showDatePicker(inputStartDate));
        inputEndDate.setOnClickListener(v -> showDatePicker(inputEndDate));


        // DRAW CHART
        lineChart = findViewById(R.id.line_chart);
        // Create sample data for the chart
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 15));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 40));
        // Add more entries as needed
        // Change Label to right type of Chart
        LineDataSet dataSet = new LineDataSet(entries, "Humid");
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

        xValues = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

        lineChart.setData(lineData);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(9);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);

        //Y Value
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setAxisMaximum(100);
        leftYAxis.setTextColor(Color.WHITE);
        leftYAxis.setLabelCount(5);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setDrawLabels(true);
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawLabels(false);


        lineChart.animateXY(2000, 2000); // Animation duration
        lineChart.invalidate(); // Refreshes the chart

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Show a Toast message with the selected entry's value
                InfoChartFragment fragment = InfoChartFragment.newInstance("Humid", Float.toString(e.getY()), "cm");

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

        datePickerDialog.show();
    }

}