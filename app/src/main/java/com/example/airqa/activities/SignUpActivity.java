package com.example.airqa.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airqa.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    // define the global variable
    TextView change_to_login_button;
    // Add button Move previous activity
    MaterialButton signup_button;

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Save language
        SharedPreferences sharedPreferences1 = getSharedPreferences("preferences", MODE_PRIVATE);
        String savedLanguage = sharedPreferences1.getString("language", "");
        setLocale(SignUpActivity.this,savedLanguage);
        // by ID we can use each component which id is assign in xml
        // file use findViewById() to get the Button and textview.
        signup_button = (MaterialButton) findViewById(R.id.signup_button);
        change_to_login_button = (TextView) findViewById(R.id.change_to_login);
        TextInputEditText username = (TextInputEditText)findViewById(R.id.usernameInputText);
        TextInputEditText email = (TextInputEditText)findViewById(R.id.emailInputText);
        TextInputEditText password = (TextInputEditText)findViewById(R.id.passwordInputText);
        TextInputEditText password_conf = (TextInputEditText)findViewById(R.id.repasswordInputText);

        WebView myWebView = (WebView) findViewById(R.id.signup_webview);
        WebSettings webSettings = myWebView.getSettings();
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        // Change to login button add click-listener
        change_to_login_button.setOnClickListener(v -> {
            // Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining
            // the components you are targeting. Intent to start an activity called SecondActivity with the following code:
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            // start the activity connect to the specified class
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        signup_button.setOnClickListener(v ->
                {
                    /*Intent intent = new Intent(SignUpActivity.this, LoadingScreen.class);
                    startActivity(intent);*/
                    myWebView.setWebViewClient(new WebViewClient(){
                        boolean buttonClicked = false;
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if(!buttonClicked){
                                Log.d("button",username.getText().toString());
                                myWebView.loadUrl("javascript:document.querySelector('a[class=\\\"btn waves-effect waves-light\\\"]').click()");
                                buttonClicked = true;
                            }
                            else{
                                String fillFormJS = "javascript:(function() {" +
                                        "var form = document.getElementById('kc-register-form');" +
                                        "var username = form.elements['username'];" +
                                        "var email = form.elements['email'];" +
                                        "var password = form.elements['password'];" +
                                        "var password_conf = form.elements['password-confirm'];" +
                                        "   username.value = '" + username.getText().toString() + "';" +
                                        "   email.value = '" + email.getText().toString() + "';" +
                                        "   password.value = '" + password.getText().toString() + "';" +
                                        "   password_conf.value = '" + password_conf.getText().toString() + "';" +
                                        " form.submit();" +
                                        "})()";
                                myWebView.loadUrl(fillFormJS);
                            }
                            view.clearCache(true);
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // Catch web redirect event after signing up successfully to call POST for logging in
                            if (url.startsWith("https://uiot.ixxc.dev/auth/realms/master/account/")) {
                                // Switch to log in with the new username and password
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.putExtra("username", username.getText().toString() + "");
                                intent.putExtra("password", password.getText().toString() + "");
                                startActivity(intent);
                                view.clearCache(true);
                                // Remove cookies to clear the web session
                                cookieManager.removeAllCookies(null);
                                return true;
                            }
                            // If you want the WebView to load the URL, return false
                            return false;
                        }
                    });
                    myWebView.loadUrl("https://uiot.ixxc.dev/auth/realms/master/account");
                    myWebView.bringToFront();
                    //myWebView.setVisibility(View.VISIBLE);
                }
        );

    }
    private void setLocale(SignUpActivity activity, String languages){
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }
}
