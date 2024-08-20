package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.States.SignInButtonState;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.States.EmailState;
import com.example.techmarket_finalproject.Utilities.States.AddressState;
import com.example.techmarket_finalproject.Utilities.States.PasswordState;
import com.example.techmarket_finalproject.Utilities.States.PhoneState;
import com.example.techmarket_finalproject.Utilities.States.RePasswordState;
import com.example.techmarket_finalproject.Utilities.States.SignUpButtonState;
import com.example.techmarket_finalproject.Utilities.States.UsernameState;
import com.example.techmarket_finalproject.Utilities.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EmailState emailStateSignIn, emailStateSignUp;
    private PasswordState passwordStateSignIn, passwordStateSignUp;
    private RePasswordState rePasswordStateSignUp;
    private PhoneState phoneStateSignUp;
    private UsernameState usernameStateSignUp;
    private AddressState addressStateSignUp;
    private SignInButtonState signInButtonState;
    private SignUpButtonState signUpButtonState;

    private ConstraintLayout signin_page, signup_page;
    private TextView moveToSignInPage, moveToSignUpPage, forgotPassword, errorMassage;
    private CheckBox rememberMeCheckBox;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private static User currentUser;
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

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
            AppUtils.statusBarColor(this);
        }

        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();
        }

        init();

        // Check if the Intent has the extra to show the SignUp page
        if (getIntent().getBooleanExtra("showSignUp", false)) {
            signin_page.setVisibility(View.GONE);
            signup_page.setVisibility(View.VISIBLE);
        }

        signInButtonState.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailStateSignIn.getEmailInput().getText().toString().trim();
                String password = passwordStateSignIn.getPasswordInput().getText().toString().trim();
                boolean rememberMe = rememberMeCheckBox.isChecked();

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task != null && task.isSuccessful()) {
                                    errorMassage.setVisibility(View.GONE);
                                    clearSignInFields();
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseManager.updateRememberLastUserFlag(userId, rememberMe);
                                    UserUtils.fetchUserAndInitializeProductManager(LoginActivity.this, userId);
                                } else {
                                    errorMassage.setVisibility(View.VISIBLE);
                                    errorMassage.setText(task.getException().getMessage());
                                }
                            }
                        });
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        signUpButtonState.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameStateSignUp.getUsernameInput().getText().toString();
                String email = emailStateSignUp.getEmailInput().getText().toString().trim();
                String password = passwordStateSignUp.getPasswordInput().getText().toString().trim();
                String rePassword = rePasswordStateSignUp.getRePasswordInput().getText().toString().trim();
                String address = addressStateSignUp.getAddressInput().getText().toString();
                String phone = phoneStateSignUp.getPhoneInput().getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task != null && task.isSuccessful()) {
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseManager.addUserToDatabase(LoginActivity.this, userId, name, email, password, address, phone, false);
                                    signup_page.setVisibility(View.GONE);
                                    signin_page.setVisibility(View.VISIBLE);
                                    clearSignUpFields();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else {
                                    if (task != null && task.getException() != null) {
                                        Log.e("SignUpError", "Sign Up Failed", task.getException());
                                    }
                                    Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        moveToSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_page.setVisibility(View.GONE);
                signin_page.setVisibility(View.VISIBLE);
                clearSignUpFields();
            }
        });

        moveToSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin_page.setVisibility(View.GONE);
                signup_page.setVisibility(View.VISIBLE);
                clearSignInFields();
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

        TextInputEditText passwordInputSignUp = findViewById(R.id.password_input_signup);

        emailStateSignIn = new EmailState(findViewById(R.id.email_input_signin), findViewById(R.id.email_input_layout_signin));
        passwordStateSignIn = new PasswordState(findViewById(R.id.password_input_signin), findViewById(R.id.password_input_layout_signin));
        emailStateSignUp = new EmailState(findViewById(R.id.email_input_signup), findViewById(R.id.email_input_layout_signup));
        passwordStateSignUp = new PasswordState(passwordInputSignUp, findViewById(R.id.password_input_layout_signup));
        rePasswordStateSignUp = new RePasswordState(findViewById(R.id.re_password_input_signup), findViewById(R.id.re_password_input_layout_signup), passwordInputSignUp);
        phoneStateSignUp = new PhoneState(findViewById(R.id.phone_input_signup), findViewById(R.id.phone_input_layout_signup));
        usernameStateSignUp = new UsernameState(findViewById(R.id.username_input_signup), findViewById(R.id.username_input_layout_signup));
        addressStateSignUp = new AddressState(findViewById(R.id.address_input_signup), findViewById(R.id.address_input_layout_signup));

        signInButtonState = new SignInButtonState(findViewById(R.id.signin_button),
                emailStateSignIn.getEmailLayout(), passwordStateSignIn.getPasswordLayout(), emailStateSignIn.getEmailInput(), passwordStateSignIn.getPasswordInput());

        signUpButtonState = new SignUpButtonState(findViewById(R.id.signup_button),
                emailStateSignUp.getEmailLayout(), passwordStateSignUp.getPasswordLayout(), rePasswordStateSignUp.getRePasswordLayout(),
                usernameStateSignUp.getUsernameLayout(), phoneStateSignUp.getPhoneLayout(), addressStateSignUp.getAddressLayout(),
                emailStateSignUp.getEmailInput(), passwordStateSignUp.getPasswordInput(), rePasswordStateSignUp.getRePasswordInput(),
                usernameStateSignUp.getUsernameInput(), phoneStateSignUp.getPhoneInput(), addressStateSignUp.getAddressInput());

        rememberMeCheckBox = findViewById(R.id.rememberme_checkbox);
        forgotPassword = findViewById(R.id.forgot_password_button);

        signin_page = findViewById(R.id.signin_page);
        signup_page = findViewById(R.id.signup_page);

        moveToSignInPage = findViewById(R.id.signin);
        moveToSignUpPage = findViewById(R.id.signup);

        // Disable buttons initially
        signInButtonState.getButton().setEnabled(false);
        signUpButtonState.getButton().setEnabled(false);

        progressBar = findViewById(R.id.progressBar);

        errorMassage = findViewById(R.id.error_message_auth);
    }

    private void clearSignUpFields() {
        usernameStateSignUp.getUsernameInput().setText("");
        emailStateSignUp.getEmailInput().setText("");
        phoneStateSignUp.getPhoneInput().setText("");
        addressStateSignUp.getAddressInput().setText("");
        passwordStateSignUp.getPasswordInput().setText("");
        rePasswordStateSignUp.getRePasswordInput().setText("");
        usernameStateSignUp.getUsernameLayout().setError(null);
        usernameStateSignUp.getUsernameLayout().setErrorEnabled(false);
        emailStateSignUp.getEmailLayout().setError(null);
        emailStateSignUp.getEmailLayout().setErrorEnabled(false);
        phoneStateSignUp.getPhoneLayout().setError(null);
        phoneStateSignUp.getPhoneLayout().setErrorEnabled(false);
        addressStateSignUp.getAddressLayout().setError(null);
        addressStateSignUp.getAddressLayout().setErrorEnabled(false);
        passwordStateSignUp.getPasswordLayout().setError(null);
        passwordStateSignUp.getPasswordLayout().setErrorEnabled(false);
        rePasswordStateSignUp.getRePasswordLayout().setError(null);
        rePasswordStateSignUp.getRePasswordLayout().setErrorEnabled(false);
    }

    private void clearSignInFields() {
        emailStateSignIn.getEmailInput().setText("");
        passwordStateSignIn.getPasswordInput().setText("");
        emailStateSignIn.getEmailLayout().setError(null);
        emailStateSignIn.getEmailLayout().setErrorEnabled(false);
        passwordStateSignIn.getPasswordLayout().setError(null);
        passwordStateSignIn.getPasswordLayout().setErrorEnabled(false);
        errorMassage.setVisibility(View.GONE);
    }

}