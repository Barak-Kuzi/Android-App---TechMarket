package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.techmarket_finalproject.R;

public class IntroActivity extends AppCompatActivity {

    private TextView goToSignInPage;
    private AppCompatButton goToSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        initViews();
    }

    private void initViews() {
        goToSignInPage = findViewById(R.id.go_to_signin_button);
        goToSignUpPage = findViewById(R.id.go_to_signup_button);

        goToSignInPage.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        });

        goToSignUpPage.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            intent.putExtra("showSignUp", true);
            startActivity(intent);
            finish();
        });
    }
}