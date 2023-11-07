package com.example.airqa;

import static com.example.airqa.LoginActivity.activeUser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.airqa.api.ApiService;
import com.example.airqa.models.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference("username").setSummary(activeUser.getEmail());
        findPreference("password").setSummary(activeUser.getId());
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
                else if (preference.getKey().equals("password")) {
                    Toast.makeText(getActivity(), "Copied to your clipboard.", Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("User ID", preference.getSummary());
                    clipboardManager.setPrimaryClip(clipData);
                    return true; // Consume the initial click event
                }
                return false;
            }

        };

        Preference preference = findPreference("logout");
        if (preference != null) {
            preference.setOnPreferenceClickListener(preferenceClickListener);

        }
        Preference preference2 = findPreference("password");
        if (preference2 != null) {
            preference2.setOnPreferenceClickListener(preferenceClickListener);
        }

    }
}