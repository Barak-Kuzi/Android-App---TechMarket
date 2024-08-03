package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.CartAdapter;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;

import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding activityCartBinding;
    private ArrayList<Product> filteredProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {

            activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
            setContentView(activityCartBinding.getRoot());

            statusBarColor();
            setButton(user);
            initList(user);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void statusBarColor() {
        Window window = CartActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.purple_Dark));
    }

    private void initList(User user) {
        if (user.cartIsEmpty()) {
            activityCartBinding.emptyCartText.setVisibility(View.VISIBLE);
            activityCartBinding.scrollViewItems.setVisibility(View.GONE);
        } else {
            activityCartBinding.emptyCartText.setVisibility(View.GONE);
            activityCartBinding.scrollViewItems.setVisibility(View.VISIBLE);
        }

        filteredProducts = new ArrayList<>();
        filterProductsByKeys(user.getCart().keySet());
        initRecyclerView(user);
        calculateTotalPrice(user);
    }

    private void filterProductsByKeys(Set<String> keys) {
        for (String key : keys) {
            Product product = ProductManager.getProductById(key);
            if (product != null) {
                filteredProducts.add(product);
            }
        }
    }

    private void initRecyclerView(User user) {
        activityCartBinding.cartView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        activityCartBinding.cartView.setAdapter(new CartAdapter(filteredProducts, user, user.getCart(), new UpdateQuantityProductsListener() {
            @Override
            public void update() {
                calculateTotalPrice(user);
            }
        }));
    }

    private void calculateTotalPrice(User user) {
        double percentTax = 0.02;
        double deliveryPrice = 10;

        double totalCartPrice = user.getTotalPrice(filteredProducts);

        double tax = (double) Math.round(totalCartPrice * percentTax * 100) / 100;
        double total = (double) Math.round((totalCartPrice + tax + deliveryPrice) * 100) / 100;
        double itemTotal = (double) Math.round((totalCartPrice + tax) * 100) / 100;

        activityCartBinding.totalFeeText.setText("$" + itemTotal);
        activityCartBinding.taxText.setText("$" + tax);
        activityCartBinding.deliveryText.setText("$" + deliveryPrice);
        activityCartBinding.totalOrderText.setText("$" + total);
    }

    private void setButton(User user) {
        activityCartBinding.backButtonViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });
    }
}