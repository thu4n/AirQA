package com.example.airqa.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.airqa.R;

public class InfoChartFragment extends Fragment {

    private static final String ARG_VALUE = "arg_value";
    private static final String ARG_TYPE = "arg_type";
    private static final String ARG_UNIT = "arg_unit";

    private String valueType;
    private String value;
    private String unit;

    public InfoChartFragment() {
        // Required empty public constructor
    }

    public static InfoChartFragment newInstance(String type, String value, String unit) {
        InfoChartFragment fragment = new InfoChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putString(ARG_VALUE, value);
        args.putString(ARG_UNIT, unit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            value = getArguments().getString(ARG_VALUE);
            valueType = getArguments().getString(ARG_TYPE);
            unit = getArguments().getString(ARG_UNIT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_chart, container, false);
        TextView typeTextView = view.findViewById(R.id.typeChart);
        TextView valueTextView = view.findViewById(R.id.valueChart);
        TextView unitTextView = view.findViewById(R.id.unitChart); // Add TextView for unit

        // Display the received type, value, and unit in the TextViews
        typeTextView.setText(valueType);
        valueTextView.setText(value);
        unitTextView.setText(unit); // Set the unit value in the TextView

        return view;
    }
}
