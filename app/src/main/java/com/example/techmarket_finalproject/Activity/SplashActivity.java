package com.example.techmarket_finalproject.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

public class SplashActivity extends AppCompatActivity {

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

        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();
        }

        AppCompatImageView splashImage = findViewById(R.id.icon);
        startAnimation(splashImage);
    }

    private void startApp() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseManager.getRememberLastUserFlag(userId, new GenericCallBack<Boolean>() {
                @Override
                public void onResponse(Boolean rememberLastUser) {
                    if (rememberLastUser) {
                        UserUtils.fetchUserAndInitializeProductManager(SplashActivity.this, userId);
                    } else {
                        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    Log.e("SplashActivity", "Failed to get rememberLastUser flag: " + databaseError.getMessage());
                    Toast.makeText(SplashActivity.this, "Failed to get rememberLastUser flag.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    private void startAnimation(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setY(-displayMetrics.heightPixels / 2 - view.getHeight());
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view
                .animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .translationY(0)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        startApp();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
    }

}