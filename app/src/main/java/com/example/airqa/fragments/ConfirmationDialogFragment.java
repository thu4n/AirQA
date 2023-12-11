package com.example.airqa.fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.airqa.activities.LoginActivity.PREF_PASSWORD;
import static com.example.airqa.activities.LoginActivity.PREF_REF_TOKEN;
import static com.example.airqa.activities.LoginActivity.PREF_TOKEN;
import static com.example.airqa.activities.LoginActivity.PREF_UNAME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.airqa.R;
import com.example.airqa.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationDialogFragment extends DialogFragment {


    public ConfirmationDialogFragment() {
        // Required empty public constructor
    }


    public static ConfirmationDialogFragment newInstance(String message) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("message");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, perform the action
                        performLogout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void performLogout() {
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_TOKEN, "");
        editor.putString(PREF_REF_TOKEN, "");
        editor.putString(PREF_UNAME,"");
        editor.putString(PREF_PASSWORD,"");
        editor.commit();
        Toast.makeText(requireContext(),  "Logging out...", Toast.LENGTH_SHORT).show();
        // move to splash screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}