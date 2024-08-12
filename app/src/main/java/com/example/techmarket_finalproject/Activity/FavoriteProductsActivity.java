package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.FavoriteProductsAdapter;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityFavoriteProductsBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteProductsActivity extends AppCompatActivity {
    private ActivityFavoriteProductsBinding activityFavoriteProductsBinding;
    private ArrayList<Product> favoriteProducts;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();

        if (user != null) {

            favoriteProducts = new ArrayList<>();
            filterProductsByKeys(user.getFavoriteProducts());

            AppUtils.statusBarColor(this);
            initViews();

            AppUtils.initNavigationBar(this, activityFavoriteProductsBinding.bottomNavigationBar.getRoot());
            activityFavoriteProductsBinding.bottomNavigationBar.getRoot().setSelectedItemId(R.id.menu_favorites);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        activityFavoriteProductsBinding = ActivityFavoriteProductsBinding.inflate(getLayoutInflater());
        setContentView(activityFavoriteProductsBinding.getRoot());

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

}