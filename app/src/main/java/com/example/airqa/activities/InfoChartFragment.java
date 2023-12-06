package com.example.airqa.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airqa.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VALUE = "arg_value";

    // TODO: Rename and change types of parameters
    private String value;

    public InfoChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param value Parameter 1.
     * @return A new instance of fragment InfoChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoChartFragment newInstance(String value) {
        InfoChartFragment fragment = new InfoChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            value = getArguments().getString(ARG_VALUE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_chart, container, false);
        TextView numberTextView = view.findViewById(R.id.numberTextView);

        // Display the received number in the TextView
        numberTextView.setText(String.valueOf(value));
        return view;
    }
}