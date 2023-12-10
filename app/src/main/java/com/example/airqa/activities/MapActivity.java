package com.example.airqa.activities;
import com.example.airqa.R;
import com.example.airqa.api.ApiHandler;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity {

    private MapView map = null;
    private FrameLayout fragmentContainer;
    LinearLayout dynamicContent,bottomNavBar;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    public List<String> assetIds = new ArrayList<>();
    public static List<WeatherAsset> weatherAssets = new ArrayList<>();
    public WeatherAsset weatherAsset;

    //public static List<String> weatherAssetIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Handle navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                //Intent intent = new Intent(getApplicationContext(), FeatureActivity.class);
                startActivity(new Intent(getApplicationContext(), FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else {
                return false;
            }
        });

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) findViewById(R.id.map);
        fragmentContainer = findViewById(R.id.fragment_container);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.getController().setZoom(19.0);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.setMultiTouchControls(true);
        // Get the access token and then from it, set the map center point
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
        String access_token = sharedPreferences.getString("access_token","");
        Log.d("access",access_token);
        Context context = this;
        getAllAsset(access_token);

    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        TextView assetName = dialog.findViewById(R.id.assetName);
        TextView tempVal = dialog.findViewById(R.id.assetIdTempValue);
        TextView humidVal = dialog.findViewById(R.id.assetIdHumidValue);
        TextView rainfallVal = dialog.findViewById(R.id.assetIdRainfallValue);
        TextView windSpeedVal = dialog.findViewById(R.id.assetIdWindSpeedValue);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        assetName.setText(weatherAsset.getName());
        double temp = weatherAsset.getAttributes().getTemperature().getValue();
        Log.d("temp", String.valueOf(temp));
        tempVal.setText( String.valueOf(temp));
        double humid = weatherAsset.getAttributes().getHumidity().getValue();
        humidVal.setText(String.valueOf(humid));
        double rainfall = weatherAsset.getAttributes().getRainfall().getValue();
        rainfallVal.setText(String.valueOf(rainfall));
        double windSpeed = weatherAsset.getAttributes().getWindSpeed().getValue();
        windSpeedVal.setText(String.valueOf(windSpeed));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        Button showAllBtn = dialog.findViewById(R.id.showAllBtn);
        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("weatherAsset", weatherAsset);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        Log.d("click marker", "show dialog");
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Material3_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDetach();
    }

    //get data in asset
    public void getAssetInfo(String access_token, String id){
        Call<WeatherAsset> call = ApiService.apiService.getAssetInfo("Bearer " + access_token, id);
        Log.e("ok2", access_token + "");
        call.enqueue(new Callback<WeatherAsset>() {
            @Override
            public void onResponse(Call<WeatherAsset> call, Response<WeatherAsset> response) {
                if(response.isSuccessful()){
                    assert response.body() != null; // make sure the body isn't null
                    weatherAsset = response.body();
                    Double x = weatherAsset.getAttributes().getLocation().getValue().getCoordinates().get(0);
                    Double y = weatherAsset.getAttributes().getLocation().getValue().getCoordinates().get(1);
                    Log.e("coor", x.toString() + " " + y.toString());
                    setMap(x,y);
                }
                else {

                }
            }
            @Override
            public void onFailure(Call<WeatherAsset> call, Throwable t) {

                Log.e("ok1",t.toString());
            }
        });
    }
    public void getAllWeatherAsset(List<String> assetIDs,String access_token){
        if(weatherAssets.size() < 4){
            for(String assetId : assetIDs){
                Call<WeatherAsset> call = ApiService.apiService.getAssetInfo("Bearer " + access_token, assetId);
                call.enqueue(new Callback<WeatherAsset>() {
                    @Override
                    public void onResponse(Call<WeatherAsset> call, Response<WeatherAsset> response) {
                        if(response.isSuccessful()){
                            assert response.body() != null; // make sure the body isn't null
                            weatherAssets.add(response.body());
                            Log.d("epochWhen1stGet", weatherAsset.getAttributes().getTemperature().getTimestamp() + "");
                        }
                        else {

                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherAsset> call, Throwable t) {

                        Log.e("ok1",t.toString());
                    }
                });
            }
        }
        else return;

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
                    getAssetInfo(access_token, assetIds.get(0));
                    getAllWeatherAsset(assetIds,access_token);
                    Log.e("id0",assetIds.get(0));

                }
                else {
                    Toast.makeText(MapActivity.this, "You do not have permission!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Asset>> call, Throwable t) {
                Log.e("failedCall",t.toString());
            }
        });
    }
    private void setMap(double x, double y){
        GeoPoint point = new GeoPoint(y , x);
        map.getController().setCenter(point);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_TOP);
        map.getOverlays().add(startMarker);
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                // Show the fragment container when the marker is clicked
                // Replace the fragment container with your desired fragment
                Log.d("click marker", "clicked marker");
                showBottomDialog();
                return true; // To consume the event and prevent other actions (like showing the InfoWindow)
            }
        });
    }
}