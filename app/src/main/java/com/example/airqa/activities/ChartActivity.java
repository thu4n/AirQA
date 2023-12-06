package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.airqa.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private LineChart lineChart, lineChart_2;
    private List<String> xValues;

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

        // DRAW CHART
        lineChart = findViewById(R.id.line_chart);
        lineChart_2 = findViewById(R.id.line_chart_2);
        // Create sample data for the chart
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 15));
        entries.add(new Entry(5, 0));
        entries.add(new Entry(6, 40));
        // Add more entries as needed

        LineDataSet dataSet = new LineDataSet(entries, "PM10");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);

        xValues = Arrays.asList("21-12", "22-12", "23-12", "24-12", "25-12", "26-12", "27-12");

        lineChart.setData(lineData);
        lineChart.getDescription().setText("Chart Description");
        lineChart.getDescription().setPosition(150f, 15f);
        lineChart.getAxisLeft().setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues)); // xValues is assumed to be your list of x-axis labels or values
        xAxis.setLabelCount(5); // Set the number of labels to be displayed on the x-axis


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0); // Set the minimum value for the y-axis
        yAxis.setAxisMaximum(100); // Set the maximum value for the y-axis
        yAxis.setAxisLineColor(Color.BLACK); // Set the color of the y-axis line
        yAxis.setYOffset(5f); // Set the offset from the y-axis
        yAxis.setLabelCount(5); // Set the number of labels to be displayed on the y-axis


        lineChart.animateXY(2000, 2000); // Animation duration
        lineChart.invalidate(); // Refreshes the chart
    }
}