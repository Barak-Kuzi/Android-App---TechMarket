package com.example.techmarket_finalproject.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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
import androidx.appcompat.widget.AppCompatImageView;

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

    private AppCompatImageView splashImage;

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
            getSupportActionBar().hide();
        }

        startAnimation(splashImage);
    }

    private void startApp() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseManager.getRememberLastUserFlag(userId, new GenericCallBack<Boolean>() {
                @Override
                public void onResponse(Boolean rememberLastUser) {
                    if (rememberLastUser) {
                        DatabaseManager.getUserFromDatabase(userId, new UserCallBack() {
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