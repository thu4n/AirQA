package com.example.airqa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.preference.PreferenceFragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingsActivity extends BaseActivity {
    LinearLayout dynamicContent,bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //dynamically include the  current activity layout into baseActivity layout.now all the view of baseactivity is   accessible in current activity.
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottomNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_settings, null);
        dynamicContent.addView(wizard);

        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.settings);

        // Change the corresponding icon and text color on nav button click.

        rb.setTextColor(Color.parseColor("#1E9CE1"));
        rb.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.primary)));


        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.idFrameLayout, new SettingsFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }

    }


}
