package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ValidationManagement;
import com.example.techmarket_finalproject.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    private FirebaseAuth firebaseAuth;
    private String email;
    private ValidationManagement validationManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        init();

        activityForgotPasswordBinding.backButtonForgotPassword.setOnClickListener(v -> finish());
        activityForgotPasswordBinding.resetPasswordButton.setEnabled(false);

        activityForgotPasswordBinding.emailInputForgotPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validate email and enable/disable the reset button
                email = s.toString().trim();
                if (validationManagement.emailFieldIsValid(email)) {
                    activityForgotPasswordBinding.resetPasswordButton.setEnabled(true);
                    activityForgotPasswordBinding.emailInputLayoutForgotPassword.setError(null);
                } else {
                    activityForgotPasswordBinding.resetPasswordButton.setEnabled(false);
                    activityForgotPasswordBinding.emailInputLayoutForgotPassword.setError(validationManagement.getEmailErrorMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        //Reset Button Listener
        activityForgotPasswordBinding.resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = activityForgotPasswordBinding.emailInputForgotPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    DatabaseManager.checkEmailExistsInDatabase(email, ForgotPasswordActivity.this, new GenericCallBack<Boolean>() {
                        @Override
                        public void onResponse(Boolean emailExists) {
                            if (emailExists) {
                                ResetPassword();
                            } else {
                                activityForgotPasswordBinding.emailInputLayoutForgotPassword.setError("No user found with this email.");
                            }
                        }

                        @Override
                        public void onFailure(DatabaseError databaseError) {
                            Toast.makeText(ForgotPasswordActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    activityForgotPasswordBinding.emailInputLayoutForgotPassword.setError("Email field can't be empty");
                }
            }
        });

    }

    private void init() {
        activityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPasswordBinding.getRoot());
        AppUtils.statusBarColor(this);
        firebaseAuth = FirebaseAuth.getInstance();
        validationManagement = new ValidationManagement();
    }

    private void ResetPassword() {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}