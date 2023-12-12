package com.example.airqa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    public static List<WeatherAsset> weatherAssets = new ArrayList<>();
    public static WeatherAsset weatherAsset;
    public List<String> assetIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
        String access_token = sharedPreferences.getString("access_token","");
        Log.d("GetTokenFromLogin",access_token);
        Context context = this;
        getAllAsset(access_token);
    }
    void pendingTransition(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish ();
            }
        }, 2000);
    }
    public void getAllWeatherAsset(List<String> assetIDs, String access_token){
        if(weatherAssets.size() < 4){ // to make sure it doesn't add more duplicates to this list upon re-creating
            Toast.makeText(SplashScreen.this, getResources().getString(R.string.Getting_assets_information), Toast.LENGTH_SHORT).show();
            for(String assetId : assetIDs){
                Call<WeatherAsset> call = ApiService.apiService.getAssetInfo("Bearer " + access_token, assetId);
                call.enqueue(new Callback<WeatherAsset>() {
                    @Override
                    public void onResponse(Call<WeatherAsset> call, Response<WeatherAsset> response) {
                        if(response.isSuccessful()){
                            assert response.body() != null; // make sure the body isn't null
                            weatherAssets.add(response.body());
                            if(response.body().getId().equals(assetIDs.get(0))){
                                weatherAsset = response.body();
                            }
                            if(assetId.equals(assetIDs.get(assetIDs.size()-1))){
                                pendingTransition();
                            }
                        }
                        else {
                            Toast.makeText(SplashScreen.this, getResources().getString(R.string.Failed_to_get_an_asset), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherAsset> call, Throwable t) {

                        Log.e("ok1",t.toString());
                    }
                });
            }
        }
        else {
            pendingTransition();
        }
    }
    public void getAllAsset(String access_token){
        // Default value for the body, do not change this query
        String rawJsonQuery = "{\n" +
                "    \"realm\": {\n" +
                "        \"name\": \"master\"\n" +
                "    },\n" +
                "    \"select\": {\n" +
                "        \"attributes\": [\n" +
                "            \"location\",\n" +
                "            \"direction\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"attributes\": {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"name\": {\n" +
                "                    \"predicateType\": \"string\",\n" +
                "                    \"value\": \"location\"\n" +
                "                },\n" +
                "                \"meta\": [\n" +
                "                    {\n" +
                "                        \"name\": {\n" +
                "                            \"predicateType\": \"string\",\n" +
                "                            \"value\": \"showOnDashboard\"\n" +
                "                        },\n" +
                "                        \"negated\": true\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": {\n" +
                "                            \"predicateType\": \"string\",\n" +
                "                            \"value\": \"showOnDashboard\"\n" +
                "                        },\n" +
                "                        \"value\": {\n" +
                "                            \"predicateType\": \"boolean\",\n" +
                "                            \"value\": true\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        RequestBody rawJsonBody = RequestBody.create(MediaType.parse("application/json"), rawJsonQuery);
        Call<List<Asset>> call = ApiService.apiService.getAllAsset("Bearer " + access_token, rawJsonBody);

        call.enqueue(new Callback<List<Asset>>() {
            @Override
            public void onResponse(Call<List<Asset>> call, Response<List<Asset>> response) {
                if(response.isSuccessful()){
                    List<Asset> assets = response.body();
                    for(Asset asset : assets){
                        if(asset.getType().equals("WeatherAsset")){ // WeatherAsset is the one that contains temperature,humidity...
                            String id = asset.getId();
                            assetIds.add(id);
                            Log.d("assetID",id);
                        }
                    }
                    getAllWeatherAsset(assetIds,access_token);
                    Log.e("id0",assetIds.get(0));

                }
                else {
                    Toast.makeText(SplashScreen.this, getResources().getString(R.string.Something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Asset>> call, Throwable t) {
                Log.e("failedCall",t.toString());
            }
        });
    }
}