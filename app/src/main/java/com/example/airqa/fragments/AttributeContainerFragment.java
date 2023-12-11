package com.example.airqa.fragments;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airqa.R;

public class AttributeContainerFragment extends Fragment {
    private Drawable icon;
    private String title;
    private String value;
    private String unit;
    private String description;
    private String avgValue;
    public static AttributeContainerFragment newInstance(Drawable icon, String title, String value, String unit, String description, String avgValue) {
        AttributeContainerFragment fragment = new AttributeContainerFragment();
        fragment.icon = icon;
        fragment.title = title;
        fragment.value = value;
        fragment.unit = unit;
        fragment.description = description;
        fragment.avgValue = avgValue;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_stat_container, container, false);
// Initialize views
        ImageView iconView = view.findViewById(R.id.icon);
        TextView titleView = view.findViewById(R.id.title);
        TextView valueView = view.findViewById(R.id.value);
        TextView unitView = view.findViewById(R.id.unit);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView avgValueView = view.findViewById(R.id.avg_value);

        // Set values to views
        iconView.setImageDrawable(icon);
        titleView.setText(title);
        valueView.setText(value);
        unitView.setText(unit);
        descriptionView.setText(description);
        avgValueView.setText(avgValue);

        return view;
    }
}
