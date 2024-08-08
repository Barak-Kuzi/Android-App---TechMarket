package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.getUserFromDatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Interfaces.UserCallBack;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    // Splash screen duration in milliseconds
    private static final long SPLASH_DURATION = 2000;

    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        splashImage = findViewById(R.id.icon);

        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();}

        animateSplashScreen();

        // Delay the transition to the LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    getUserFromDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserCallBack() {
                        @Override
                        public void onSuccess(User user) {
                            ProductManager.initialize(SplashActivity.this, new GenericCallBack<ArrayList<Product>>() {
                                @Override
                                public void onResponse(ArrayList<Product> response) {
                                    LoginActivity.setCurrentUser(user);
                                    DatabaseManager.addNewListenerForUser();
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(DatabaseError error) {
                                    Toast.makeText(SplashActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(DatabaseError error) {
                            Toast.makeText(SplashActivity.this, "Failed to load user.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }
        }, SPLASH_DURATION);
    }

    private void animateSplashScreen() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.setDuration(1500);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setStartOffset(3500);
        fadeOut.setDuration(1500);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);

        splashImage.startAnimation(animation);
    }
}