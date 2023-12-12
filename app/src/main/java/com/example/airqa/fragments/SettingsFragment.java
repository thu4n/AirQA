package com.example.airqa.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.airqa.activities.LoginActivity.activeUser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.airqa.R;
import com.example.airqa.activities.MapActivity;
import com.example.airqa.fragments.ConfirmationDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Save language
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String savedLanguage = sharedPreferences.getString("language", "");
        setLocale(SettingsFragment.this,savedLanguage);
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
                String confirmationMessage = getResources().getString(R.string.Logout_setting);
                ConfirmationDialogFragment dialog = ConfirmationDialogFragment.newInstance(confirmationMessage);
                dialog.show(requireActivity().getSupportFragmentManager(), "");
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
        findPreference("Language_key").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals("Language_key")) {
                    String selectedLanguage = (String) newValue;

                    // Xử lý khi người dùng chọn một ngôn ngữ cụ thể
                    if (selectedLanguage.equals("English")) {
                        saveLanguage("default");
                        setLocale(SettingsFragment.this,"default");

                    } else if (selectedLanguage.equals("Vietnamese")) {
                        saveLanguage("vi");
                        setLocale(SettingsFragment.this,"vi");

                    } else if (selectedLanguage.equals("Japanese")) {
                        saveLanguage("ja");
                        setLocale(SettingsFragment.this,"ja");

                    }
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    startActivity(intent);
                    // Return true để cập nhật giá trị preference
                    return true;
                }
                return false;

            }

        });
        Preference preference = findPreference("logout");
        if (preference != null) {
            preference.setOnPreferenceClickListener(preferenceClickListener);

        }
        Preference preference2 = findPreference("user_id");
        if (preference2 != null) {
            preference2.setOnPreferenceClickListener(preferenceClickListener);
        }

        EditTextPreference numberPreference = findPreference("number_preference");
        if (numberPreference != null) {
            numberPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)}); // Limit to 3 characters
                }
            });
        }

    }
    private void setLocale(SettingsFragment activity, String languages){
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    // Lưu trạng thái ngôn ngữ
    private void saveLanguage(String language) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.apply();
    }
}