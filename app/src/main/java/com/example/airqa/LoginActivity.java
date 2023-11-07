package com.example.airqa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context; //
import android.content.SharedPreferences; //

import androidx.appcompat.app.AppCompatActivity;

import com.example.airqa.api.ApiService;
import com.example.airqa.models.AuthResponse;
import com.example.airqa.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // define the global variable
    TextView change_to_signup_button;
    MaterialButton login_button;
    // Add button Move to next Activity and previous Activity
    TextInputEditText username;
    TextInputEditText password;
    CheckBox rememberMe;
    // create data to storage username and password
    public static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_TOKEN = "";
    private static final String PREF_REF_TOKEN = "";
    private final String DefaultUnameValue = "";
    private String UnameValue;
    private final String DefaultPasswordValue = "";
    private String PasswordValue; //

    private String access_token;
    private String refresh_token;

    private final String default_token = "";
    private final String default_ref_token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // by ID we can use each component which id is assign in xml
        // file use findViewById() to get the both Button and textview
        MaterialButton login_button = (MaterialButton) findViewById(R.id.login_button);
        TextView change_to_signup_button = (TextView) findViewById(R.id.change_to_signup);

        // Change to sign up button add click-listener
        change_to_signup_button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            // start the activity connect to the specified class
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        username = (TextInputEditText)findViewById(R.id.usernameInputText);
        password = (TextInputEditText)findViewById(R.id.passwordInputText);
        rememberMe = (CheckBox)findViewById(R.id.remember_me);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(username.getText().toString(),password.getText().toString());
            }
        });
        checkBox();
        getUserInfo(access_token);
    }

    private void logIn(String username, String password){
        Call<AuthResponse> call = ApiService.apiService.userLogin("openremote", username, password, "password");
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()) {
                    access_token = response.body().getAccess_token();
                    refresh_token = response.body().getRefresh_token();
                    getUserInfo(response.body().getAccess_token());
                    //
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("check","true");
                    editor.apply();
                    //


                    Log.e("ok", response.body().getAccess_token() + "");
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "An error has occurred, please try again.", Toast.LENGTH_SHORT).show();
                Log.e("fail", t.getMessage() + "");
            }
        });
    }
    private void getUserInfo(String access_token){
        Call<User> call = ApiService.apiService.getUserInfo("Bearer " + access_token);
        Log.e("ok2", access_token + "");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logged in with " + response.body().getEmail(), Toast.LENGTH_SHORT).show();
                    Log.e("ok", response.body().getEmail() + "");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(response.code() == 401){

                    Log.e("not_ok", response.message() + "");
                    refreshToken(refresh_token);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("fail", t.getMessage() + "");
            }
        });
    }
    private void refreshToken(String refresh_token){
        Toast.makeText(LoginActivity.this, "Requesting new token...", Toast.LENGTH_SHORT).show();
        Call<AuthResponse> call = ApiService.apiService.refreshToken("openremote",refresh_token,"refresh_token");
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    Log.e("ok",  "New token: " + response.body().getAccess_token());
                    getUserInfo(response.body().getAccess_token());
                }
                else if(response.code() == 401){

                    Log.e("not_ok",  "Token expired");
                    Toast.makeText(LoginActivity.this, "Token expired, please log in manually", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("fail", t.getMessage() + "");
            }
        });
    }
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = username.getText().toString();
        PasswordValue = password.getText().toString();
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.putString(PREF_TOKEN, access_token);
        editor.putString(PREF_REF_TOKEN, refresh_token);
        editor.commit();
    }
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value

        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        access_token = settings.getString(PREF_TOKEN,default_token);
        refresh_token = settings.getString(PREF_REF_TOKEN, default_ref_token);
        username.setText(UnameValue);
        password.setText(PasswordValue);
    }
    @Override
    public void onPause() {
        super.onPause();
        if(rememberMe.isChecked())
        {savePreferences();}
    }
    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }
    private void checkBox(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String check = sharedPreferences.getString("check","");
        if(check.equals("true")) {
            loadPreferences();

            //logIn(username.getText().toString(),password.getText().toString());
            //  login_button.performClick();
        }
    }
}