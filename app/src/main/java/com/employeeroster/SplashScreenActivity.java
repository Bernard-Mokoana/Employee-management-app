package com.employeeroster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Set up fade-in animation for logo or splash elements
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView logoImageView = findViewById(R.id.logo_image);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500); // 1.5 seconds
        fadeIn.setFillAfter(true);
        logoImageView.startAnimation(fadeIn);

        // Set up handler to start LoginActivity after the timeout
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_SCREEN_TIMEOUT);
    }
}