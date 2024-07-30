package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Util.DatabaseManager.getUserFromDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techmarket_finalproject.Model.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Util.UserCallBack;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;

public class EditProfileActivity extends AppCompatActivity {

    //private TextView fullNameLabel, usernameProfile, emailProfile, phoneProfile, addressProfile;
    private AppCompatButton updateProfileButton;
    private TextInputEditText username_input_signup, email_input_signup, phone_input_signup, address_input_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        init();

        // Get user data from database
        String userId = getIntent().getStringExtra("userId");
        getUserFromDatabase(userId, new UserCallBack() {
            @Override
            public void onSuccess(User user) {
                username_input_signup.setText(user.getName());
                email_input_signup.setText(user.getEmail());
                phone_input_signup.setText(user.getPhone());
                address_input_signup.setText(user.getAddress());
            }

            @Override
            public void onFailure(DatabaseError error) {
                // Handle error
            }
        });

        updateProfileButton.setOnClickListener(v -> {
            // Update user profile
            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
        });

    }

    private void init() {
//        fullNameLabel = findViewById(R.id.fullName_label);
//        usernameProfile = findViewById(R.id.username_text_profile);
//        emailProfile = findViewById(R.id.email_text_profile);
//        phoneProfile = findViewById(R.id.phone_text_profile);
//        addressProfile = findViewById(R.id.address_text_profile);

        username_input_signup = findViewById(R.id.username_input_signup);
        email_input_signup = findViewById(R.id.email_input_signup);
        phone_input_signup = findViewById(R.id.phone_input_signup);
        address_input_signup = findViewById(R.id.address_input_signup);


        updateProfileButton = findViewById(R.id.update_profile_button);
    }

}