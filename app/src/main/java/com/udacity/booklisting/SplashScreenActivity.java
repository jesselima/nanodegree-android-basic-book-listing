package com.udacity.booklisting;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Load the SplashScreenActivity for 2 seconds and starts BookListActivity.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToWelcomeActivity();
            }
        }, 2000);
    }

    /**
     * When this method is called it start BookListActivity
     */
    private void goToWelcomeActivity(){
        Intent intent = new Intent(this, BookListActivity.class);
        startActivity(intent);
        finish();
    }
}
