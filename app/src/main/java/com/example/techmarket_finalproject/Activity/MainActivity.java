package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.PopularProductsAdapter;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;

import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.Models.Product;

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
            initRecyclerView(user);
            bottomNavigation(user);

            // Initialize database with products if not already done
            DatabaseManager.initializeDatabaseWithProducts(this);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void bottomNavigation(User user) {
        activityMainBinding.cartMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        activityMainBinding.profileMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        activityMainBinding.favoritesMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteProductsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        activityMainBinding.viewAllProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView(User user) {
        ArrayList<Product> products = ProductManager.getAllProducts();

        activityMainBinding.popularProductsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.popularProductsView.setAdapter(new PopularProductsAdapter(products, user));
    }

}