package com.example.airqa;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("logout")) {
                    String confirmationMessage = "Are you sure you want to log out?";
                    ConfirmationDialogFragment dialog = ConfirmationDialogFragment.newInstance(confirmationMessage);
                    Context context = getContext();
                    dialog.show(requireActivity().getSupportFragmentManager(), "confirmation_dialog");
                    return true; // Consume the initial click event
                }
                return false;
            }
        };

        Preference preference = findPreference("logout");
        if (preference != null) {
            preference.setOnPreferenceClickListener(preferenceClickListener);
        }

    }
}