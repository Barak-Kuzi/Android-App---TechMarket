package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.Utilities.States.AddressState;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.States.ConfirmButtonState;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.States.PasswordState;
import com.example.techmarket_finalproject.Utilities.States.PhoneState;
import com.example.techmarket_finalproject.Utilities.States.RePasswordState;
import com.example.techmarket_finalproject.Utilities.States.UsernameState;
import com.example.techmarket_finalproject.databinding.ActivityEditProfileBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding activityEditProfileBinding;
    private User user;

    private UsernameState usernameState;
    private PasswordState passwordState;
    private RePasswordState rePasswordState;
    private AddressState addressState;
    private PhoneState phoneState;
    private ConfirmButtonState confirmButtonState;

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
            initDetails();
            confirmButtonState.getButton().setEnabled(false);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

        confirmButtonState.getButton().setOnClickListener(v -> {
            String newUsername = usernameState.getUsernameInput().getText().toString();
            String newAddress = addressState.getAddressInput().getText().toString();
            String newPhone = phoneState.getPhoneInput().getText().toString();
            String newPassword = passwordState.getPasswordInput().getText().toString();

            if (firebaseUser != null && user != null) {
                Map<String, Object> userMap = new HashMap<>();
                if (!newUsername.equals(user.getName())) {
                    userMap.put("name", newUsername);
                }
                if (!newAddress.equals(user.getAddress())) {
                    userMap.put("address", newAddress);
                }
                if (!newPhone.equals(user.getPhone())) {
                    userMap.put("phone", newPhone);
                }
                if (!newPassword.equals(user.getPassword())) {
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
        usernameState = new UsernameState(activityEditProfileBinding.usernameInputSignup, activityEditProfileBinding.usernameInputLayoutSignup);
        passwordState = new PasswordState(activityEditProfileBinding.passwordInputSignup, activityEditProfileBinding.passwordInputLayoutSignup);
        rePasswordState = new RePasswordState(activityEditProfileBinding.rePasswordInputSignup, activityEditProfileBinding.rePasswordInputLayoutSignup, activityEditProfileBinding.passwordInputSignup);
        addressState = new AddressState(activityEditProfileBinding.addressInputSignup, activityEditProfileBinding.addressInputLayoutSignup);
        phoneState = new PhoneState(activityEditProfileBinding.phoneInputSignup, activityEditProfileBinding.phoneInputLayoutSignup);

        confirmButtonState = new ConfirmButtonState(activityEditProfileBinding.confirmButtonEditProfile,
                activityEditProfileBinding.passwordInputLayoutSignup, activityEditProfileBinding.rePasswordInputLayoutSignup,
                activityEditProfileBinding.usernameInputLayoutSignup, activityEditProfileBinding.phoneInputLayoutSignup,
                activityEditProfileBinding.addressInputLayoutSignup, activityEditProfileBinding.passwordInputSignup,
                activityEditProfileBinding.rePasswordInputSignup, activityEditProfileBinding.usernameInputSignup,
                activityEditProfileBinding.phoneInputSignup, activityEditProfileBinding.addressInputSignup);

    }

    private void initDetails() {
        usernameState.getUsernameInput().setText(user.getName());
        addressState.getAddressInput().setText(user.getAddress());
        phoneState.getPhoneInput().setText(user.getPhone());
        passwordState.getPasswordInput().setText(user.getPassword());
        rePasswordState.getRePasswordInput().setText(user.getPassword());
    }

    private void updatePassword(FirebaseUser firebaseUser, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), user.getPassword());
        firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseUser.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                    if (!updateTask.isSuccessful()) {
                        String errorMessage = updateTask.getException() != null ? updateTask.getException().getMessage() : "Unknown error";
                        Toast.makeText(this, "Failed to update password: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Toast.makeText(this, "Re-authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}