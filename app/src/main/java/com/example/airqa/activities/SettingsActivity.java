package com.example.airqa.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.airqa.R;

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
                    .replace(R.id.idFrameLayout, new SettingsFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }

    }


}
