package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addUserToDatabase;
import static com.example.techmarket_finalproject.Utilities.DatabaseManager.getUserFromDatabase;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Interfaces.UserCallBack;
import com.example.techmarket_finalproject.Utilities.ValidationManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayoutSignIn, passwordLayoutSignIn,
            emailLayoutSignUp, passwordLayoutSignUp, usernameLayoutSignUp,
            re_passwordLayoutSignUp, addressLayoutSignUp, phoneLayoutSignUp;

    private TextInputEditText emailInputSignIn, passwordInputSignIn,
            emailInputSignUp, passwordInputSignUp, usernameInputSignUp,
            re_passwordInputSignUp, addressInputSignUp, phoneInputSignUp;

    private Button signInButton, signUpButton;
    private ConstraintLayout signin_page, signup_page;
    private TextView moveToSignInPage, moveToSignUpPage;

    private FirebaseAuth firebaseAuth;

    private final ValidationManagement validationManagement = new ValidationManagement();

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
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
            Window window = LoginActivity.this.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.green));
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

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task != null && task.isSuccessful()) {
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    getUserFromDatabase(userId, new UserCallBack() {
                                        @Override
                                        public void onSuccess(User user) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(DatabaseError error) {

                                        }
                                    });

//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameInputSignUp.getText().toString();
                String email = emailInputSignUp.getText().toString().trim();
                String password = passwordInputSignUp.getText().toString().trim();
                String rePassword = re_passwordInputSignUp.getText().toString().trim();
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

        emailLayoutSignIn = findViewById(R.id.email_input_layout_signin);
        passwordLayoutSignIn = findViewById(R.id.password_input_layout_signin);

        emailInputSignIn = findViewById(R.id.email_input_signin);
        passwordInputSignIn = findViewById(R.id.password_input_signin);

        usernameLayoutSignUp = findViewById(R.id.username_input_layout_signup);
        emailLayoutSignUp = findViewById(R.id.email_input_layout_signup);
        passwordLayoutSignUp = findViewById(R.id.password_input_layout_signup);
        re_passwordLayoutSignUp = findViewById(R.id.re_password_input_layout_signup);
        addressLayoutSignUp = findViewById(R.id.address_input_layout_signup);
        phoneLayoutSignUp = findViewById(R.id.phone_input_layout_signup);

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

        // TextWatchers for real-time validation
        emailInputSignIn.addTextChangedListener(new ValidationTextWatcher(emailInputSignIn));
        passwordInputSignIn.addTextChangedListener(new ValidationTextWatcher(passwordInputSignIn));

        emailInputSignUp.addTextChangedListener(new ValidationTextWatcher(emailInputSignUp));
        passwordInputSignUp.addTextChangedListener(new ValidationTextWatcher(passwordInputSignUp));
        re_passwordInputSignUp.addTextChangedListener(new ValidationTextWatcher(re_passwordInputSignUp));
        usernameInputSignUp.addTextChangedListener(new ValidationTextWatcher(usernameInputSignUp));
        addressInputSignUp.addTextChangedListener(new ValidationTextWatcher(addressInputSignUp));
        phoneInputSignUp.addTextChangedListener(new ValidationTextWatcher(phoneInputSignUp));

        // Disable buttons initially
        signInButton.setEnabled(false);
        signUpButton.setEnabled(false);

    }

    private void clearFields() {
        usernameInputSignUp.setText("");
        emailInputSignUp.setText("");
        phoneInputSignUp.setText("");
        addressInputSignUp.setText("");
        passwordInputSignUp.setText("");
        re_passwordInputSignUp.setText("");
    }

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String name = usernameInputSignUp.getText().toString();
            String email = emailInputSignUp.getText().toString().trim();
            String password = passwordInputSignUp.getText().toString().trim();
            String rePassword = re_passwordInputSignUp.getText().toString().trim();
            String address = addressInputSignUp.getText().toString();
            String phone = phoneInputSignUp.getText().toString();

            int viewId = view.getId();
            if (viewId == R.id.email_input_signin) {
                validateEmailSignIn();
            } else if (viewId == R.id.password_input_signin) {
                validatePasswordSignIn();
            } else if (viewId == R.id.email_input_signup) {
                validateEmailSignUp();
            } else if (viewId == R.id.password_input_signup) {
                validatePasswordSignUp();
            } else if (viewId == R.id.re_password_input_signup) {
                validateRePasswordSignUp();
            } else if (viewId == R.id.username_input_signup) {
                validateUsernameSignUp();
            } else if (viewId == R.id.address_input_signup) {
                validateAddressSignUp();
            } else if (viewId == R.id.phone_input_signup) {
                validatePhoneSignUp();
            }

            // Check if all fields are valid to enable/disable buttons
            signInButton.setEnabled(validationManagement.allSingInFieldsAreValid(
                    emailInputSignIn.getText().toString().trim(),
                    passwordInputSignIn.getText().toString().trim()
            ));

            signUpButton.setEnabled(validationManagement.allSingUpFieldsAreValid(
                    emailInputSignUp.getText().toString().trim(),
                    passwordInputSignUp.getText().toString().trim(),
                    re_passwordInputSignUp.getText().toString().trim(),
                    usernameInputSignUp.getText().toString().trim(),
                    phoneInputSignUp.getText().toString().trim(),
                    addressInputSignUp.getText().toString().trim()
            ));

        }
    }

    private void validateEmailSignIn() {
        String email = emailInputSignIn.getText().toString().trim();
        validationManagement.validateEmail(email);
        if (!validationManagement.emailIsValid()) {
            emailLayoutSignIn.setError(validationManagement.getEmailErrorMessage());
        } else {
            emailLayoutSignIn.setError(null);
            emailLayoutSignIn.setErrorEnabled(false);
        }
    }

    private void validatePasswordSignIn() {
        String password = passwordInputSignIn.getText().toString().trim();
        validationManagement.validatePassword(password);
        if (!validationManagement.passwordIsValid()) {
            passwordLayoutSignIn.setError(validationManagement.getPasswordErrorMessage());
        } else {
            passwordLayoutSignIn.setError(null);
            passwordLayoutSignIn.setErrorEnabled(false);
        }
    }

    private void validateEmailSignUp() {
        String email = emailInputSignUp.getText().toString().trim();
        validationManagement.validateEmail(email);
        if (!validationManagement.emailIsValid()) {
            emailLayoutSignUp.setError(validationManagement.getEmailErrorMessage());
        } else {
            emailLayoutSignUp.setError(null);
            emailLayoutSignUp.setErrorEnabled(false);
        }
    }

    private void validatePasswordSignUp() {
        String password = passwordInputSignUp.getText().toString().trim();
        validationManagement.validatePassword(password);
        if (!validationManagement.passwordIsValid()) {
            passwordLayoutSignUp.setError(validationManagement.getPasswordErrorMessage());
        } else {
            passwordLayoutSignUp.setError(null);
            passwordLayoutSignUp.setErrorEnabled(false);
        }
    }

    private void validateRePasswordSignUp() {
        String password = passwordInputSignUp.getText().toString().trim();
        String rePassword = re_passwordInputSignUp.getText().toString().trim();
        validationManagement.validateRePassword(password, rePassword);
        if (!validationManagement.rePasswordIsValid()) {
            re_passwordLayoutSignUp.setError(validationManagement.getRePasswordErrorMessage());
        } else {
            re_passwordLayoutSignUp.setError(null);
            re_passwordLayoutSignUp.setErrorEnabled(false);
        }
    }

    private void validateUsernameSignUp() {
        String username = usernameInputSignUp.getText().toString().trim();
        validationManagement.validateUsername(username);
        if (!validationManagement.usernameIsValid()) {
            usernameLayoutSignUp.setError(validationManagement.getUsernameErrorMessage());
        } else {
            usernameLayoutSignUp.setError(null);
            usernameLayoutSignUp.setErrorEnabled(false);
        }
    }

    private void validateAddressSignUp() {
        String address = addressInputSignUp.getText().toString().trim();
        validationManagement.validateAddress(address);
        if (!validationManagement.addressIsValid()) {
            addressLayoutSignUp.setError(validationManagement.getAddressErrorMessage());
        } else {
            addressLayoutSignUp.setError(null);
            addressLayoutSignUp.setErrorEnabled(false);
        }
    }

    private void validatePhoneSignUp() {
        String phone = phoneInputSignUp.getText().toString().trim();
        validationManagement.validatePhone(phone);
        if (!validationManagement.phoneIsValid()) {
            phoneLayoutSignUp.setError(validationManagement.getPhoneErrorMessage());
        } else {
            phoneLayoutSignUp.setError(null);
            phoneLayoutSignUp.setErrorEnabled(false);
        }
    }

}




