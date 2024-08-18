package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ValidationManagement;
import com.example.techmarket_finalproject.databinding.ActivityEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding activityEditProfileBinding;
    private final ValidationManagement validationManagement = new ValidationManagement();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditProfileBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(activityEditProfileBinding.getRoot());
        EdgeToEdge.enable(this);

        user = LoginActivity.getCurrentUser();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (user != null) {

            AppUtils.statusBarColor(this);
            initTextWatchers();

            activityEditProfileBinding.usernameInputSignup.setText(user.getName());
            activityEditProfileBinding.addressInputSignup.setText(user.getAddress());
            activityEditProfileBinding.phoneInputSignup.setText(user.getPhone());
            activityEditProfileBinding.passwordInputSignup.setText(user.getPassword());
            activityEditProfileBinding.rePasswordInputSignup.setText(user.getPassword());
            activityEditProfileBinding.confirmButtonEditProfile.setEnabled(false);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

        activityEditProfileBinding.confirmButtonEditProfile.setOnClickListener(v -> {
            String newUsername = activityEditProfileBinding.usernameInputSignup.getText().toString();
            String newAddress = activityEditProfileBinding.addressInputSignup.getText().toString();
            String newPhone = activityEditProfileBinding.phoneInputSignup.getText().toString();
            String newPassword = activityEditProfileBinding.passwordInputSignup.getText().toString();

            if (firebaseUser != null && user != null) {
                Map<String, Object> userMap = new HashMap<>();
                if (!newUsername.equals(user.getName())) {
                    userMap.put("name", newUsername);
                } else if (!newAddress.equals(user.getAddress())) {
                    userMap.put("address", newAddress);
                } else if (!newPhone.equals(user.getPhone())) {
                    userMap.put("phone", newPhone);
                } else if (!newPassword.equals(user.getPassword())) {
                    updatePassword(firebaseUser, newPassword);
                    userMap.put("password", newPassword);
                }

                DatabaseManager.updateUserInDatabase(EditProfileActivity.this, user.getUserId(), userMap);

                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });

        activityEditProfileBinding.cancelButtonEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            finish();
        });

    }

    private void initTextWatchers() {
        activityEditProfileBinding.usernameInputSignup.addTextChangedListener(new ValidationTextWatcher(activityEditProfileBinding.usernameInputSignup));
        activityEditProfileBinding.addressInputSignup.addTextChangedListener(new ValidationTextWatcher(activityEditProfileBinding.addressInputSignup));
        activityEditProfileBinding.phoneInputSignup.addTextChangedListener(new ValidationTextWatcher(activityEditProfileBinding.phoneInputSignup));
        activityEditProfileBinding.passwordInputSignup.addTextChangedListener(new ValidationTextWatcher(activityEditProfileBinding.passwordInputSignup));
        activityEditProfileBinding.rePasswordInputSignup.addTextChangedListener(new ValidationTextWatcher(activityEditProfileBinding.rePasswordInputSignup));
    }

    private void updatePassword(FirebaseUser firebaseUser, String newPassword) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show();
            }
        });
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
            String name = activityEditProfileBinding.usernameInputSignup.getText().toString();
            String password = activityEditProfileBinding.passwordInputSignup.getText().toString().trim();
            String rePassword = activityEditProfileBinding.rePasswordInputSignup.getText().toString().trim();
            String address = activityEditProfileBinding.addressInputSignup.getText().toString();
            String phone = activityEditProfileBinding.phoneInputSignup.getText().toString();

            int viewId = view.getId();
            if (viewId == R.id.password_input_signup) {
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
            activityEditProfileBinding.confirmButtonEditProfile.setEnabled(validationManagement
                    .allEditProfileFieldsAreValid(name, address, phone, password, rePassword)
            );

        }
    }

    private void validatePasswordSignUp() {
        String password = activityEditProfileBinding.passwordInputSignup.getText().toString().trim();
        validationManagement.validatePassword(password);
        if (!validationManagement.passwordIsValid()) {
            activityEditProfileBinding.passwordInputLayoutSignup.setError(validationManagement.getPasswordErrorMessage());
        } else {
            activityEditProfileBinding.passwordInputLayoutSignup.setError(null);
            activityEditProfileBinding.passwordInputLayoutSignup.setErrorEnabled(false);
        }
    }

    private void validateRePasswordSignUp() {
        String password = activityEditProfileBinding.passwordInputSignup.getText().toString().trim();
        String rePassword = activityEditProfileBinding.rePasswordInputSignup.getText().toString().trim();
        validationManagement.validateRePassword(password, rePassword);
        if (!validationManagement.rePasswordIsValid()) {
            activityEditProfileBinding.rePasswordInputLayoutSignup.setError(validationManagement.getRePasswordErrorMessage());
        } else {
            activityEditProfileBinding.rePasswordInputLayoutSignup.setError(null);
            activityEditProfileBinding.rePasswordInputLayoutSignup.setErrorEnabled(false);
        }
    }

    private void validateUsernameSignUp() {
        String username = activityEditProfileBinding.usernameInputSignup.getText().toString().trim();
        validationManagement.validateUsername(username);
        if (!validationManagement.usernameIsValid()) {
            activityEditProfileBinding.usernameInputLayoutSignup.setError(validationManagement.getUsernameErrorMessage());
        } else {
            activityEditProfileBinding.usernameInputLayoutSignup.setError(null);
            activityEditProfileBinding.usernameInputLayoutSignup.setErrorEnabled(false);
        }
    }

    private void validateAddressSignUp() {
        String address = activityEditProfileBinding.addressInputSignup.getText().toString().trim();
        validationManagement.validateAddress(address);
        if (!validationManagement.addressIsValid()) {
            activityEditProfileBinding.addressInputLayoutSignup.setError(validationManagement.getAddressErrorMessage());
        } else {
            activityEditProfileBinding.addressInputLayoutSignup.setError(null);
            activityEditProfileBinding.addressInputLayoutSignup.setErrorEnabled(false);
        }
    }

    private void validatePhoneSignUp() {
        String phone = activityEditProfileBinding.phoneInputSignup.getText().toString().trim();
        validationManagement.validatePhone(phone);
        if (!validationManagement.phoneIsValid()) {
            activityEditProfileBinding.phoneInputLayoutSignup.setError(validationManagement.getPhoneErrorMessage());
        } else {
            activityEditProfileBinding.phoneInputLayoutSignup.setError(null);
            activityEditProfileBinding.phoneInputLayoutSignup.setErrorEnabled(false);
        }
    }

}