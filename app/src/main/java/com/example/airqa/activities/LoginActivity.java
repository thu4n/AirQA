package com.example.airqa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airqa.R;
import com.example.airqa.api.ApiService;
import com.example.airqa.models.AuthResponse;
import com.example.airqa.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

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
    public static final String PREF_UNAME = "Username";
    public static final String PREF_PASSWORD = "Password";
    public static String PREF_TOKEN = "";
    public static final String PREF_REF_TOKEN = "";
    private  String DefaultUnameValue = "";
    private String UnameValue;
    private  String DefaultPasswordValue = "";
    private String PasswordValue; //

    private String access_token;
    private String refresh_token;

    private  String default_token = "";
    private  String default_ref_token = "";

    public static User activeUser;

    private String help_id;
    private String help_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences1 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLanguage = sharedPreferences1.getString("language", "");
        setLocale(LoginActivity.this,savedLanguage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Save language
        // by ID we can use each component which id is assign in xml
        // file use findViewById() to get the both Button and textview
        MaterialButton login_button = (MaterialButton) findViewById(R.id.login_button);
        TextView change_to_signup_button = (TextView) findViewById(R.id.change_to_signup);
        TextView forgot_password = (TextView) findViewById(R.id.forgot_password);

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

        // Get credentials after successfully signed up
        Intent get_intent = getIntent();
        String get_username = get_intent.getStringExtra("username");
        String get_password = get_intent.getStringExtra("password");
        boolean after_signup = false;
        if(get_username != null && get_password != null){
            username.setText(get_username);
            password.setText(get_password);
            after_signup = true;
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Sign_up_successful), Toast.LENGTH_SHORT).show();
        }

        login_button.setOnClickListener(view -> logIn(username.getText().toString(),password.getText().toString()));

        forgot_password.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Fill all information below");
            LinearLayout layout = new LinearLayout(LoginActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText emailInput = new EditText(LoginActivity.this);
            emailInput.setHint("Your email address");
            layout.addView(emailInput);

            final EditText idInput = new EditText(LoginActivity.this);
            idInput.setHint("Your user ID");
            layout.addView(idInput);
            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    help_email = emailInput.getText().toString();
                    help_id = idInput.getText().toString();
                    //do something here
                    Toast.makeText(LoginActivity.this, "Email sent! ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        if(!after_signup){
            checkBox();
            //getUserInfo(access_token);
        }
    }

    public void logIn(String username, String password){
        Call<AuthResponse> call = ApiService.apiService.userLogin("openremote", username, password, "password");
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()) {
                    access_token = response.body().getAccess_token();
                    refresh_token = response.body().getRefresh_token();
                    getUserInfo(response.body().getAccess_token());
                    //
                    // shared pref

                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token",access_token);
                    editor.putString("refresh_token",refresh_token);
                    editor.putString("check","true");
                    editor.apply();

                    Log.e("ok", response.body().getAccess_token() + "");
                }
                else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Wrong_username_password_), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.An_error_has_occurred), Toast.LENGTH_SHORT).show();
                Log.e("fail", t.getMessage() + "");
            }
        });
    }
    public  void getUserInfo(String access_token){
        Call<User> call = ApiService.apiService.getUserInfo("Bearer " + access_token);
        Log.e("ok2", access_token + "");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Logged_in_with) + response.body().getEmail(), Toast.LENGTH_SHORT).show();
                    Log.e("ok", response.body().getEmail() + "");
                    activeUser = response.body();
                  // move to splash screen
                    Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(response.code() == 401){
                    Log.e("not_ok", response.message() + "");
                    refreshToken(refresh_token);
                }
                // add more conditions here later
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("fail", t.getMessage() + "");
            }
        });
    }
    public void refreshToken(String refresh_token){
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.Requesting_new_token), Toast.LENGTH_SHORT).show();
        Call<AuthResponse> call = ApiService.apiService.refreshToken("openremote",refresh_token,"refresh_token");
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    Log.e("ok",  getResources().getString(R.string.New_token) + response.body().getAccess_token());
                    getUserInfo(response.body().getAccess_token());
                }
                else if(response.code() == 401){
                    Log.e("not_ok",  getResources().getString(R.string.Token_expired));
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Token_expired_info), Toast.LENGTH_SHORT).show();
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
        //loadPreferences();
        // Why the fuck do you load here??
    }
    private void checkBox(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String check = sharedPreferences.getString("check","");
        if(check.equals("true")) {
            loadPreferences();
        }
    }
    private void setLocale(LoginActivity activity, String languages){
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}