package com.example.airqa.activities;

import static com.example.airqa.activities.LoginActivity.activeUser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.airqa.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference("email").setSummary(activeUser.getEmail());
        findPreference("user_id").setSummary(activeUser.getId());
        findPreference("username").setSummary(activeUser.getUsername());
        Date date = new Date(activeUser.getCreatedOn());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate  = formatter.format(date);
        findPreference("createdOn").setSummary(formattedDate);
        findPreference("realmId").setSummary(activeUser.getRealmId());
        Preference.OnPreferenceClickListener preferenceClickListener = preference -> {
            if (preference.getKey().equals("logout")) {
                String confirmationMessage = "Are you sure you want to log out?";
                ConfirmationDialogFragment dialog = ConfirmationDialogFragment.newInstance(confirmationMessage);
                dialog.show(requireActivity().getSupportFragmentManager(), "confirmation_dialog");
                return true; // Consume the initial click event
            }
            else if (preference.getKey().equals("user_id")) {
                Toast.makeText(getActivity(), "Copied to your clipboard.", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("User ID", preference.getSummary());
                clipboardManager.setPrimaryClip(clipData);
                return true; // Consume the initial click event
            }
            return false;
        };

        Preference preference = findPreference("logout");
        if (preference != null) {
            preference.setOnPreferenceClickListener(preferenceClickListener);

        }
        Preference preference2 = findPreference("user_id");
        if (preference2 != null) {
            preference2.setOnPreferenceClickListener(preferenceClickListener);
        }
    }
}