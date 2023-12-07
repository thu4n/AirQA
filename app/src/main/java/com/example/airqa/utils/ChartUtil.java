package com.example.airqa.utils;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.airqa.R;
import com.example.airqa.activities.InfoChartFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.List;

public class ChartUtil {

    public static LineChart createChart(@NonNull Context context, @NonNull List<Entry> entries,
                                            @NonNull List<String> xValues, @NonNull FragmentManager fragmentManager,
                                            @ColorRes int lineColor, @ColorRes int fillColor) {
        LineChart lineChart = new LineChart(context);

        LineDataSet dataSet = new LineDataSet(entries, "Data");
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setFillColor(lineColor);
        dataSet.setColor(fillColor);
        dataSet.setFillAlpha(255);
        dataSet.setDrawCircles(false);

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(xValues.size());
        xAxis.setGranularity(1);
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

        lineChart.animateXY(2000, 2000);
        lineChart.invalidate();

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Show a fragment with the selected entry's value
                showChartInfoFragment(fragmentManager, e.getY());
            }

            @Override
            public void onNothingSelected() {
                // Do something when nothing is selected (optional)
            }
        });

        return lineChart;
    }

    private static void showChartInfoFragment(@NonNull FragmentManager fragmentManager, float value) {
        Fragment fragment = InfoChartFragment.newInstance(Float.toString(value));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_pmi, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
