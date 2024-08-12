package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding activityProfileBinding;
    private User user;
    private CircleImageView profileImage;
    private BottomNavigationView bottomNavigationView;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Toast.makeText(ProfileActivity.this, "Uploading image...", Toast.LENGTH_SHORT).show();
                    Uri selectedImageUri = result.getData().getData();
                    DatabaseManager.uploadImageToFirebaseStorage(user, selectedImageUri, new GenericCallBack<String>() {
                        @Override
                        public void onResponse(String imageUrl) {
                            updateUserProfileImage(imageUrl);
                        }

                        @Override
                        public void onFailure(DatabaseError databaseError) {
                            Toast.makeText(ProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();

        if (user != null) {

            activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
            setContentView(activityProfileBinding.getRoot());
            EdgeToEdge.enable(this);

            initViews();

            AppUtils.initNavigationBar(this, bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);

            activityProfileBinding.fullNameLabel.setText(user.getName());
            activityProfileBinding.usernameTextProfile.setText(user.getName());
            activityProfileBinding.emailTextProfile.setText(user.getEmail());
            activityProfileBinding.phoneTextProfile.setText(user.getPhone());
            activityProfileBinding.addressTextProfile.setText(user.getAddress());

            if (user.getProfileImageUrl() != null) {
                Glide.with(this).load(user.getProfileImageUrl()).into(profileImage);
            }

            activityProfileBinding.uploadImageButton.setOnClickListener(v -> openImagePicker());

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

        activityProfileBinding.purchaseHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PurchaseHistoryActivity.class));
            }
        });

        activityProfileBinding.wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FavoriteProductsActivity.class));
            }
        });

        activityProfileBinding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        activityProfileBinding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.updateRememberLastUserFlag(user.getUserId(), false);
                user.setRememberMe(false);
                FirebaseAuth.getInstance().signOut();
                LoginActivity.setCurrentUser(null);
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        profileImage = findViewById(R.id.profile_image_view);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void updateUserProfileImage(String imageUrl) {
        DatabaseManager.updateUserProfileImage(user.getUserId(), imageUrl, new GenericCallBack<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response) {
                    user.setProfileImageUrl(imageUrl);
                    Glide.with(ProfileActivity.this).load(imageUrl).into(profileImage);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile image.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to update profile image.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}