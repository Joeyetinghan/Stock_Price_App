package com.example.stockpriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    /**
     * This is my LaunchActivity.
     *
     * @param savedInstanceState - idk
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}


