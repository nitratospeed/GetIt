package com.example.getit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                String userSession = prefs.getString("Email","");

                if (userSession != ""){
                    Intent i = new Intent(LauncherActivity.this, MainActivity.class);
                    finish();  //Kill the activity from which you will go to next activity
                    startActivity(i);
                }

                else {
                    Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                    finish();  //Kill the activity from which you will go to next activity
                    startActivity(i);
                }
            }}, 1000);
    }
}
