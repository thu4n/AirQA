package com.example.airqa.activities;
import com.example.airqa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.osmdroid.views.overlay.Marker;

public class MarkerInfoFragment extends BottomSheetDialogFragment {

    private static final String ARG_MARKER_TITLE = "arg_marker_title";
    private static final String ARG_MARKER_DESCRIPTION = "arg_marker_description";

    private String markerTitle;
    private String markerDescription;

    public MarkerInfoFragment() {
        // Required empty public constructor
    }

    public static MarkerInfoFragment newInstance(Marker marker) {
        MarkerInfoFragment fragment = new MarkerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MARKER_TITLE, marker.getTitle());
        args.putString(ARG_MARKER_DESCRIPTION, marker.getSnippet());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            markerTitle = "ASSET TITLE";
            markerDescription = "ASSET DESCRIPTION";
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_info, container, false);

//        TextView titleTextView = view.findViewById(R.id.titleTextView);
//        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
            Button showAssetBtn = view.findViewById(R.id.showAssetBtn);
            showAssetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click event here
                    // For example, navigate to another activity
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

        // Display marker information in TextViews
//        titleTextView.setText(markerTitle);
//        descriptionTextView.setText(markerDescription);

        return view;
    }
}
