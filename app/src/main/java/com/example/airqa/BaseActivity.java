package com.example.airqa;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    RadioGroup bottomNav;
    RadioButton home, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        bottomNav = (RadioGroup) findViewById(R.id.radioGroup1);
        home = (RadioButton) findViewById(R.id.home);
        settings = (RadioButton) findViewById(R.id.settings);
        bottomNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent intent;
                if (checkedId == R.id.home) {
                    Log.i("change bottom nav", "move to Home" +  checkedId);
                    intent=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (checkedId == R.id.settings) {
                    Log.i("change bottom nav", "move to Settings" + checkedId);
                    intent = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }
}