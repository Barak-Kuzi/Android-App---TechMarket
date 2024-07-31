package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.techmarket_finalproject.Model.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Util.DatabaseManager;
import com.example.techmarket_finalproject.Util.ValidationManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private AppCompatButton confirmUpdateButton, cancelUpdateButton;

    private TextInputLayout passwordLayoutSignUp, usernameLayoutSignUp,
            re_passwordLayoutSignUp, addressLayoutSignUp, phoneLayoutSignUp;

    private TextInputEditText passwordInputSignUp, usernameInputSignUp,
            re_passwordInputSignUp, addressInputSignUp, phoneInputSignUp;

    private final ValidationManagement validationManagement = new ValidationManagement();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_edit_profile);
            init();

            usernameInputSignUp.setText(user.getName());
            addressInputSignUp.setText(user.getAddress());
            phoneInputSignUp.setText(user.getPhone());
            passwordInputSignUp.setText(user.getPassword());
            re_passwordInputSignUp.setText(user.getPassword());
            confirmUpdateButton.setEnabled(false);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

        confirmUpdateButton.setOnClickListener(v -> {
            // Update user profile
            String newUsername = usernameInputSignUp.getText().toString();
            String newAddress = addressInputSignUp.getText().toString();
            String newPhone = phoneInputSignUp.getText().toString();
            String newPassword = passwordInputSignUp.getText().toString();

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

                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        cancelUpdateButton.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

    }

    private void init() {
        usernameLayoutSignUp = findViewById(R.id.username_input_layout_signup);
        addressLayoutSignUp = findViewById(R.id.address_input_layout_signup);
        phoneLayoutSignUp = findViewById(R.id.phone_input_layout_signup);
        passwordLayoutSignUp = findViewById(R.id.password_input_layout_signup);
        re_passwordLayoutSignUp = findViewById(R.id.re_password_input_layout_signup);

        usernameInputSignUp = findViewById(R.id.username_input_signup);
        addressInputSignUp = findViewById(R.id.address_input_signup);
        phoneInputSignUp = findViewById(R.id.phone_input_signup);
        passwordInputSignUp = findViewById(R.id.password_input_signup);
        re_passwordInputSignUp = findViewById(R.id.re_password_input_signup);

        confirmUpdateButton = findViewById(R.id.update_profile_button);
        cancelUpdateButton = findViewById(R.id.cancel_update_profile_button);

        // TextWatchers for real-time validation
        usernameInputSignUp.addTextChangedListener(new ValidationTextWatcher(usernameInputSignUp));
        addressInputSignUp.addTextChangedListener(new ValidationTextWatcher(addressInputSignUp));
        phoneInputSignUp.addTextChangedListener(new ValidationTextWatcher(phoneInputSignUp));
        passwordInputSignUp.addTextChangedListener(new ValidationTextWatcher(passwordInputSignUp));
        re_passwordInputSignUp.addTextChangedListener(new ValidationTextWatcher(re_passwordInputSignUp));
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
            String name = usernameInputSignUp.getText().toString();
            String password = passwordInputSignUp.getText().toString().trim();
            String rePassword = re_passwordInputSignUp.getText().toString().trim();
            String address = addressInputSignUp.getText().toString();
            String phone = phoneInputSignUp.getText().toString();

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
            confirmUpdateButton.setEnabled(validationManagement
                    .allEditProfileFieldsAreValid(name, address, phone, password, rePassword)
            );

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