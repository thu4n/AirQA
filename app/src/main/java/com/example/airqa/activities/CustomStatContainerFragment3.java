package com.example.airqa.activities;

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
import com.example.airqa.R; // Replace with your actual package name

public class CustomStatContainerFragment3 extends Fragment {
    private Drawable icon;
    private String title;
    private String currentValue;

    public static CustomStatContainerFragment3 newInstance(Drawable icon, String title, String currentValue) {
        CustomStatContainerFragment3 fragment = new CustomStatContainerFragment3();
        fragment.icon = icon;
        fragment.title = title;
        fragment.currentValue = currentValue;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_stat_container3, container, false);

        // Initialize views
        ImageView iconView = view.findViewById(R.id.icon);
        TextView titleView = view.findViewById(R.id.title);
        TextView valueView = view.findViewById(R.id.current_value);

        // Set values to views
        iconView.setImageDrawable(icon);
        titleView.setText(title);
        valueView.setText(currentValue);

        return view;
    }
}
