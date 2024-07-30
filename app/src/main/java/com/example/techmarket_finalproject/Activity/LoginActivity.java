package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Util.DatabaseManager.addUserToDatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.techmarket_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInputSignIn, passwordInputSignIn,
            emailInputSignUp, passwordInputSignUp, usernameInputSignUp,
            re_passwordInputSignUp, addressInputSignUp, phoneInputSignUp;
    private Button signInButton, signUpButton;
    private ConstraintLayout signin_page, signup_page;
    private TextView moveToSignInPage, moveToSignUpPage;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        init();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInputSignIn.getText().toString().trim();
                String password = passwordInputSignIn.getText().toString().trim();
                if (!email.equals("") && !password.equals("")) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task != null && task.isSuccessful()) {
                                        String userId = firebaseAuth.getCurrentUser().getUid();
                                        moveToActivity(MainActivity.class, userId);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    } else {
                                        // Login failed, display an error message
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Type an email and a password of at least 6 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailInputSignUp.getText().toString().equals("") && !passwordInputSignUp.getText().toString().equals("")) {
                    String name = usernameInputSignUp.getText().toString();
                    String email = emailInputSignUp.getText().toString().trim();
                    String password = passwordInputSignUp.getText().toString().trim();
                    String address = addressInputSignUp.getText().toString();
                    String phone = phoneInputSignUp.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task != null && task.isSuccessful()) {
                                        String userId = firebaseAuth.getCurrentUser().getUid();
                                        addUserToDatabase(LoginActivity.this, userId, name, email, password, address, phone);
                                        signup_page.setVisibility(View.GONE);
                                        signin_page.setVisibility(View.VISIBLE);
                                        clearFields();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    } else {
                                        // Sign up failed, display an error message
                                        Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Enter password and e-mail to sign up", Toast.LENGTH_SHORT).show();
                }
            }
        });

        moveToSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_page.setVisibility(View.GONE);
                signin_page.setVisibility(View.VISIBLE);
            }
        });

        moveToSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin_page.setVisibility(View.GONE);
                signup_page.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Apply fade animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();

        emailInputSignIn = findViewById(R.id.email_input_signin);
        passwordInputSignIn = findViewById(R.id.password_input_signin);

        usernameInputSignUp = findViewById(R.id.username_input_signup);
        emailInputSignUp = findViewById(R.id.email_input_signup);
        passwordInputSignUp = findViewById(R.id.password_input_signup);
        re_passwordInputSignUp = findViewById(R.id.re_password_input_signup);
        addressInputSignUp = findViewById(R.id.address_input_signup);
        phoneInputSignUp = findViewById(R.id.phone_input_signup);

        signInButton = findViewById(R.id.signin_button);
        signUpButton = findViewById(R.id.signup_button);

        signin_page = findViewById(R.id.signin_page);
        signup_page = findViewById(R.id.signup_page);

        moveToSignInPage = findViewById(R.id.signin);
        moveToSignUpPage = findViewById(R.id.signup);
    }

    private void clearFields() {
        usernameInputSignUp.setText("");
        emailInputSignUp.setText("");
        passwordInputSignUp.setText("");
        re_passwordInputSignUp.setText("");
    }

    private void moveToActivity(Class<?> targetActivity, String userId) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}


