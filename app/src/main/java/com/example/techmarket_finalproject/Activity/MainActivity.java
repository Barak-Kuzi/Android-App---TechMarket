package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapter.PopularAdapter;
import com.example.techmarket_finalproject.Model.User;
import com.example.techmarket_finalproject.R;

import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private TextView welcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(activityMainBinding.getRoot());

            welcomeUser = findViewById(R.id.welcome_user_name);
            welcomeUser.setText(user.getName());

            statusBarColor();
            initRecyclerView();
            bottomNavigation(user);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void bottomNavigation(User user) {
        activityMainBinding.cartMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        activityMainBinding.profileMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("T-shirt black", "item_1", 15, 4, 500, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Smart Watch", "item_2", 10, 4.5, 450, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Phone", "item_3", 3, 4.9, 800, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Phone", "b_1", 3, 4.9, 800, "testttttttttttttttttttttttt"));

        activityMainBinding.popularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.popularView.setAdapter(new PopularAdapter(items));
    }
}