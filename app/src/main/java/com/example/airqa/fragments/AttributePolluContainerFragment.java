package com.example.airqa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airqa.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttributePolluContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AttributePolluContainerFragment extends Fragment {
    private Drawable icon;
    private String title;
    private String stat1;
    private String value1;
    private String unit1;
    private String stat2;
    private String value2;
    private String unit2;
    private String stat3;
    private String value3;
    private String unit3;

    public static AttributePolluContainerFragment newInstance(Drawable icon, String title,
                                                              String stat1, String value1, String unit1,
                                                              String stat2, String value2, String unit2,
                                                              String stat3, String value3, String unit3) {
        AttributePolluContainerFragment fragment = new AttributePolluContainerFragment();
        fragment.icon = icon;
        fragment.title = title;
        fragment.stat1 = stat1;
        fragment.value1 = value1;
        fragment.unit1 = unit1;
        fragment.stat2 = stat2;
        fragment.value2 = value2;
        fragment.unit2 = unit2;
        fragment.stat3 = stat3;
        fragment.value3 = value3;
        fragment.unit3 = unit3;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_stat_container2, container, false);

        // Initialize views
        ImageView iconView = view.findViewById(R.id.icon);
        TextView titleView = view.findViewById(R.id.title);
        TextView stat1View = view.findViewById(R.id.stat1);
        TextView value1View = view.findViewById(R.id.value1);
        TextView unit1View = view.findViewById(R.id.unit1);
        TextView stat2View = view.findViewById(R.id.stat2);
        TextView value2View = view.findViewById(R.id.value2);
        TextView unit2View = view.findViewById(R.id.unit2);
        TextView stat3View = view.findViewById(R.id.stat3);
        TextView value3View = view.findViewById(R.id.value3);
        TextView unit3View = view.findViewById(R.id.unit3);

        // Set values to views
        iconView.setImageDrawable(icon);
        titleView.setText(title);
        stat1View.setText(stat1);
        value1View.setText(value1);
        unit1View.setText(unit1);
        stat2View.setText(stat2);
        value2View.setText(value2);
        unit2View.setText(unit2);
        stat3View.setText(stat3);
        value3View.setText(value3);
        unit3View.setText(unit3);

        return view;
    }
}
