package com.nightcafeadmin.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nightcafeadmin.app.authentication.SignInActivity;
import com.nightcafeadmin.app.databases.SessionManager;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run() {

                //Get session data from local database
                sessionManager = new SessionManager(SplashActivity.this);
                HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();


                //check if user already signined

                if(sessionManager.checkLogin() == true)
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }


            }

        },4000); // splash screen delay 4 seconds

    }
}