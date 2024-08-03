package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addAllProductsToDatabase;

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
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView(User user) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("P1", "T-shirt black", R.drawable.item_1, 15, 4, 500, "Description for T-shirt black"));
        products.add(new Product("P2", "Smart Watch", R.drawable.item_2, 10, 4.5, 450, "Description for Smart Watch"));
        products.add(new Product("P3", "Phone", R.drawable.item_3, 3, 4.9, 800, "Description for Phone"));
        products.add(new Product("P4", "Phone", R.drawable.b_1, 3, 4.9, 800, "Description for Phone"));

        addAllProductsToDatabase(this, products);

        activityMainBinding.popularProductsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.popularProductsView.setAdapter(new PopularProductsAdapter(products, user));
    }
}