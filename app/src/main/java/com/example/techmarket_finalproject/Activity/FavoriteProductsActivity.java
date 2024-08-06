package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.CartAdapter;
import com.example.techmarket_finalproject.Adapters.FavoriteProductsAdapter;
import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityFavoriteProductsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavoriteProductsActivity extends AppCompatActivity {
    ActivityFavoriteProductsBinding activityFavoriteProductsBinding;
    private ArrayList<Product> favoriteProducts;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {

            favoriteProducts = new ArrayList<>();
            filterProductsByKeys(user.getFavoriteProducts());

            statusBarColor();
            initViews();
            initBottomNavigationBar();

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void statusBarColor() {
        Window window = FavoriteProductsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(FavoriteProductsActivity.this, R.color.new_green));
    }

    private void initViews() {
        activityFavoriteProductsBinding = ActivityFavoriteProductsBinding.inflate(getLayoutInflater());
        setContentView(activityFavoriteProductsBinding.getRoot());

        activityFavoriteProductsBinding.backButtonFavoriteProducts.setOnClickListener(v -> {
            Intent intent = new Intent(FavoriteProductsActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        if (user.getFavoriteProducts().isEmpty()) {
            activityFavoriteProductsBinding.emptyFavoriteProductsMessage.setVisibility(android.view.View.VISIBLE);
            activityFavoriteProductsBinding.favoriteProductsRecyclerView.setVisibility(android.view.View.GONE);
        } else {
            activityFavoriteProductsBinding.emptyFavoriteProductsMessage.setVisibility(android.view.View.GONE);
            activityFavoriteProductsBinding.favoriteProductsRecyclerView.setVisibility(android.view.View.VISIBLE);
        }

        initRecyclerView(user);
    }

    private void initRecyclerView(User user) {
        activityFavoriteProductsBinding.favoriteProductsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        activityFavoriteProductsBinding.favoriteProductsRecyclerView.setAdapter(new FavoriteProductsAdapter(favoriteProducts, user));
    }

    private void filterProductsByKeys(List<String> productIdsList) {
        for (String value : productIdsList) {
            Product product = ProductManager.getProductById(value);
            if (product != null) {
                favoriteProducts.add(product);
            }
        }
    }

    private void initBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(FavoriteProductsActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_cart) {
                Intent intent = new Intent(FavoriteProductsActivity.this, CartActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_favorites) {
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(FavoriteProductsActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else {
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_favorites);
    }
}