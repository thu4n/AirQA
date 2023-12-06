package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private LineChart lineChart;
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
        lineChart = findViewById(R.id.line_chart_pmi);
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

        LineDataSet dataSet = new LineDataSet(entries, "PM10");
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setFillColor(R.color.primary);
        dataSet.setColor(R.color.primary);
        dataSet.setFillAlpha(255);
        dataSet.setDrawCircles(false);

        LineData lineData = new LineData(dataSet);

        xValues = Arrays.asList("21-12", "22-12", "23-12", "24-12", "25-12", "26-12", "27-12");

        lineChart.setData(lineData);
        lineChart.getDescription().setText("Chart Description");
        lineChart.getDescription().setPosition(150f, 15f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(8);
        xAxis.setDrawGridLines(false);


        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setAxisMaximum(100);
        leftYAxis.setLabelCount(5);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setDrawLabels(true);
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawLabels(false);


        lineChart.animateXY(2000, 2000); // Animation duration
        lineChart.invalidate(); // Refreshes the

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Show a Toast message with the selected entry's value
                Toast.makeText(getApplicationContext(), "Value: " + e.getY(), Toast.LENGTH_SHORT).show();
                }

            @Override
            public void onNothingSelected() {
                // Do something when nothing is selected (optional)
            }
        });
    }
}