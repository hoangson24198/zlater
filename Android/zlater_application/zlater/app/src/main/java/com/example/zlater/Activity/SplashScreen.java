package com.example.zlater.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zlater.Activity.Login.LoginMethod;
import com.example.zlater.R;
import com.example.zlater.Utils.Constants;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView logo;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        logo = findViewById(R.id.logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        logo.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                if (!username.isEmpty()) {
                    Intent intentToMain = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intentToMain);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, LoginMethod.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

