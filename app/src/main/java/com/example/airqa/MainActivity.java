package com.example.airqa;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends BaseActivity  {

    public static final String PREFS_NAME = "preferences";
    Button button;
    LinearLayout dynamicContent,bottomNavBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottomNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);

        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.home);

        // Change the corresponding icon and text color on nav button click.

        rb.setTextColor(Color.parseColor("#1E9CE1"));
        rb.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.primary)));
        button = (Button)findViewById(R.id.logout_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("check","");
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}