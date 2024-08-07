package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Adapters.ImageAdapter;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ImagePicker;
import com.example.techmarket_finalproject.databinding.ActivityImageSelectionBinding;

import java.util.List;

public class ImageSelectionActivity extends AppCompatActivity {

    ActivityImageSelectionBinding activityImageSelectionBinding;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            activityImageSelectionBinding = ActivityImageSelectionBinding.inflate(getLayoutInflater());
            setContentView(activityImageSelectionBinding.getRoot());

            activityImageSelectionBinding.backButtonToAdminPanel.setOnClickListener(v -> {
                Intent intent = new Intent(ImageSelectionActivity.this, AdminActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            });

            activityImageSelectionBinding.recyclerViewImageSelection.setLayoutManager(new GridLayoutManager(ImageSelectionActivity.this, 2));

            ImagePicker.fetchAllImages(this, new ImagePicker.ImageFetchCallback() {
                @Override
                public void onSuccess(List<Uri> imageUris) {
                    imageAdapter = new ImageAdapter(imageUris, uri -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedImageUri", uri.toString());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                    activityImageSelectionBinding.recyclerViewImageSelection.setAdapter(imageAdapter);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ImageSelectionActivity.this, "Failed to load images: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}