package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView fullNameLabel, usernameProfile, emailProfile, phoneProfile, addressProfile;
    private LinearLayout homePageButton, editProfileButton, adminPanelButton;
    private AppCompatButton logoutButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_profile);
            initViews();
            initBottomNavigationBar();

            fullNameLabel.setText(user.getName());
            usernameProfile.setText(user.getName());
            emailProfile.setText(user.getEmail());
            phoneProfile.setText(user.getPhone());
            addressProfile.setText(user.getAddress());

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isAdmin()) {
                    Intent intent = new Intent(ProfileActivity.this, AdminActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "You are not an admin.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }

    private void initViews() {
        fullNameLabel = findViewById(R.id.fullName_label);
        usernameProfile = findViewById(R.id.username_text_profile);
        emailProfile = findViewById(R.id.email_text_profile);
        phoneProfile = findViewById(R.id.phone_text_profile);
        addressProfile = findViewById(R.id.address_text_profile);

        homePageButton = findViewById(R.id.home_page_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        adminPanelButton = findViewById(R.id.admin_panel_button);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void initBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_cart) {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_favorites) {
                Intent intent = new Intent(ProfileActivity.this, FavoriteProductsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_profile) {
                return true;
            } else {
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
    }
}