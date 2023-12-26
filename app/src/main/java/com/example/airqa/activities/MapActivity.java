package com.example.airqa.activities;
import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.ml.Humidity;
import com.example.airqa.ml.Temperature;
import com.example.airqa.ml.WindSpeed;
import com.example.airqa.models.DataPoint;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    // for predict
    Humidity modelH;
    Temperature modelT;
    WindSpeed modelW;
    float H,T,W;

    BottomNavigationView bottomNavigationView;
    //public static List<String> weatherAssetIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Save language
        SharedPreferences sharedPreferences1 = getSharedPreferences("preferences", MODE_PRIVATE);
        String savedLanguage = sharedPreferences1.getString("language", "");
        setLocale(MapActivity.this,savedLanguage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Handle navbar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_features) {
                //Intent intent = new Intent(getApplicationContext(), FeatureActivity.class);
                startActivity(new Intent(MapActivity.this, FeatureActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_chart) {
                startActivity(new Intent(MapActivity.this, ChartActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (item.getItemId() == R.id.bottom_settings) {
                startActivity(new Intent(MapActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else {
                return false;
            }
        });
        FloatingActionButton fabButton = findViewById(R.id.fabBtn); // Replace R.id.fabButton with your FAB ID

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPredictDialog();
            }
        });
        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) findViewById(R.id.map);
        fragmentContainer = findViewById(R.id.fragment_container);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.getController().setZoom(20.0);
        map.setMinZoomLevel(15.0);
        map.setMaxZoomLevel(22.0);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.setMultiTouchControls(true);
        // Get the access token and then from it, set the map center point
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
        String access_token = sharedPreferences.getString("access_token","");

        Double x = SplashScreen.weatherAsset.getAttributes().getLocation().getValue().getCoordinates().get(0);
        Double y = SplashScreen.weatherAsset.getAttributes().getLocation().getValue().getCoordinates().get(1);
        setMap(x,y);


        modelH = null;
        modelT = null;
        modelW = null;
        try {
            modelH = Humidity.newInstance(this);
            modelT = Temperature.newInstance(this);
            modelW = WindSpeed.newInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            PredictWeather(access_token, "5zI6XqkQVSfdgOrZ1MyWEf", "humidity");
            PredictWeather(access_token, "5zI6XqkQVSfdgOrZ1MyWEf", "temperature");
            PredictWeather(access_token, "5zI6XqkQVSfdgOrZ1MyWEf", "windSpeed");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        // Show a dialog asking the user to confirm closing the app
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    private void setLocale(MapActivity activity, String languages){
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

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

        assetName.setText(SplashScreen.weatherAsset.getName());
        double temp =  SplashScreen.weatherAsset.getAttributes().getTemperature().getValue();
        Log.d("temp", String.valueOf(temp));
        tempVal.setText( String.valueOf(temp));
        double humid =  SplashScreen.weatherAsset.getAttributes().getHumidity().getValue();
        humidVal.setText(String.valueOf(humid));
        double rainfall =  SplashScreen.weatherAsset.getAttributes().getRainfall().getValue();
        rainfallVal.setText(String.valueOf(rainfall));
        double windSpeed =  SplashScreen.weatherAsset.getAttributes().getWindSpeed().getValue();
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
                intent.putExtra("weatherAsset",  SplashScreen.weatherAsset);
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
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
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

    private void setMap(double x, double y){
        GeoPoint point = new GeoPoint(y , x);
        map.getController().setCenter(point);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_TOP);

        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setLatNorth(10.90);
        boundingBox.setLatSouth(10.835);
        boundingBox.setLonEast(106.83);
        boundingBox.setLonWest(106.78);
        map.setScrollableAreaLimitDouble(boundingBox);

        Drawable newIconDrawable = getResources().getDrawable(R.drawable.marker);
        Bitmap newIconBitmap = ((BitmapDrawable) newIconDrawable).getBitmap();

        Bitmap scaledIconBitmap = Bitmap.createScaledBitmap(newIconBitmap, 100, 100, false);
        startMarker.setIcon(new BitmapDrawable(getResources(), scaledIconBitmap));

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
    private void showPredictDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.predictsheetlayout);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

      //  TextView assetName = dialog.findViewByI;
        TextView tempVal = dialog.findViewById(R.id.assetIdTempValue1);
        TextView humidVal = dialog.findViewById(R.id.assetIdHumidValue1);
        TextView windSpeedVal = dialog.findViewById(R.id.assetIdWindSpeedValue1);
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String T1 = sharedPreferences.getString("PredTemperature", "");
        String H1 = sharedPreferences.getString("PredHumidity", "");
        String W1 = sharedPreferences.getString("PredWindSpeed", "");



        tempVal.setText( String.valueOf(T1));
        humidVal.setText(String.valueOf(H1));
        windSpeedVal.setText(String.valueOf(W1));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Log.d("click predict", "show dialog");
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Material3_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
    };
    // predict
    private void PredictWeather(String access_token, String assetId, String attributeName) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        long tenHoursAgoTimeInMillis = currentTimeInMillis - (10 * 60 * 60 * 1000);

        long endEpoch = currentTimeInMillis;
        Log.d("epoch",endEpoch + "");

        long startEpoch = tenHoursAgoTimeInMillis;
        String rawJsonQuery = "{\"type\":\"lttb\",\"fromTimestamp\":" + startEpoch + ",\"toTimestamp\":" + endEpoch + ",\"amountOfPoints\":50}";
        RequestBody rawJsonBody = RequestBody.create(MediaType.parse("application/json"), rawJsonQuery);
        Call<List<DataPoint>> call = ApiService.apiService.getDataPoint("Bearer " + access_token,rawJsonBody,assetId, attributeName);
        call.enqueue(new Callback<List<DataPoint>>() {
            @Override
            public void onResponse(Call<List<DataPoint>> call, Response<List<DataPoint>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() <= 0){
                        Toast.makeText(MapActivity.this, "No data to be shown!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("CallDataPoint", "200 OK");
                    List<Entry> entries = new ArrayList<>();
                    assert response.body() != null;
                    for(DataPoint dataPoint : response.body()){
                        entries.add(new Entry(dataPoint.getX(), dataPoint.getY()));
                    }
                    Predict(entries,attributeName);

                }
                else {
                    Toast.makeText(MapActivity.this, "You do not have permission!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DataPoint>> call, Throwable t) {
            }
        });
    }
    private void Predict(List<Entry> entries,String attributeName){
        // get data from asset
        float v0 = entries.get(entries.size() - 8).getY();
        float v1 = entries.get(entries.size() - 7).getY();
        float v2 = entries.get(entries.size() - 6).getY();
        float v3 = entries.get(entries.size() - 5).getY();
        float v4 = entries.get(entries.size() - 4).getY();
        float v5 = entries.get(entries.size() - 3).getY();
        float v6 = entries.get(entries.size() - 2).getY();
        float v7 = entries.get(entries.size() - 1).getY();
        // convert data to ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.putFloat(v0);
        byteBuffer.putFloat(v1);
        byteBuffer.putFloat(v2);
        byteBuffer.putFloat(v3);
        byteBuffer.putFloat(v4);
        byteBuffer.putFloat(v5);
        byteBuffer.putFloat(v6);
        byteBuffer.putFloat(v7);
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 8}, DataType.FLOAT32);
        inputFeature0.loadBuffer(byteBuffer);

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (attributeName.equals("humidity")) {
            // Runs model inference
            Humidity.Outputs outputs = modelH.process(inputFeature0);
            float outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatValue(0);
            H = outputFeature0;
            modelH.close();
        }

        if (attributeName.equals("temperature")) {
            // Runs model inference
            Temperature.Outputs outputs = modelT.process(inputFeature0);
            float outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatValue(0);
            T = outputFeature0;
            modelT.close();
        }

        if (attributeName.equals("windSpeed")) {
            // Runs model inference and gets result.
            WindSpeed.Outputs outputs = modelW.process(inputFeature0);
            float outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatValue(0);
            W = outputFeature0;
            modelW.close();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setMaximumFractionDigits(1);

        editor.putString("PredHumidity", String.valueOf(decimalFormat.format(H)));
        editor.putString("PredTemperature", String.valueOf(decimalFormat.format(T)));
        editor.putString("PredWindSpeed", String.valueOf(decimalFormat.format(W)));
        editor.apply();
    }
}