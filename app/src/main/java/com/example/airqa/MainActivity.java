package com.example.airqa;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.airqa.api.ApiService;
import com.example.airqa.models.weatherAsset;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity  {

    public static final String PREFS_NAME = "preferences";
    Button button;
    LinearLayout dynamicContent,bottomNavBar;
    TextView Humidity,Temp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottomNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);
        Humidity = (TextView) findViewById(R.id.humidity);
        Temp = (TextView) findViewById(R.id.temp_number);
        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.home);

        // Change the corresponding icon and text color on nav button click.

        rb.setTextColor(Color.parseColor("#1E9CE1"));
        rb.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.primary)));

        // get access token
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String check = sharedPreferences.getString("access_token","");
        // get asset info
        getAssetInfo(check);
    }
    //get data in asset
    public  void getAssetInfo(String access_token){
        Call<weatherAsset> call = ApiService.apiService.getAssetInfo("Bearer " + access_token);
        //Log.e("ok2", access_token + "");
        Log.e("ok2", access_token + "");
        call.enqueue(new Callback<weatherAsset>() {
            @Override
            public void onResponse(Call<weatherAsset> call, Response<weatherAsset> response) {
                if(response.isSuccessful()){
                    Humidity.setText(response.body().getAttributes().get("humidity").getValue().toString());
                    Temp.setText(response.body().getAttributes().get("temperature").getValue().toString());

                }
                else {

                }
                // add more conditions here later
            }
            @Override
            public void onFailure(Call<weatherAsset> call, Throwable t) {

                Log.e("ok1",t.toString());
            }
        });
    }
}