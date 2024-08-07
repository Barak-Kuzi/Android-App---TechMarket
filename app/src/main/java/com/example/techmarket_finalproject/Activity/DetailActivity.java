package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addProductToCartInDatabase;
import static com.example.techmarket_finalproject.Utilities.DatabaseManager.updateFavoriteProductsInDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ActivityDetailBinding;
import com.example.techmarket_finalproject.Models.Product;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding activityDetailBinding;
    private Product product;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        statusBarColor();
        getBundles();
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.new_green));
    }

    private void getBundles() {
        this.product = (Product) getIntent().getSerializableExtra("product");
        this.user = (User) getIntent().getSerializableExtra("user");

        if (product != null && user != null) {

            if (user.isAdmin()) {
                activityDetailBinding.editDeleteLayout.setVisibility(View.VISIBLE);
                activityDetailBinding.editProductButton.setOnClickListener(v -> {
                    Intent intent = new Intent(DetailActivity.this, AdminActivity.class);
                    intent.putExtra("product", product);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                });
            } else {
                activityDetailBinding.editDeleteLayout.setVisibility(View.GONE);
            }

            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                ImageLoader.loadImage(activityDetailBinding.itemDetailImage, product.getImageUri());
            } else {
                ImageLoader.loadImage(activityDetailBinding.itemDetailImage, product.getImageResourceId());
            }

            activityDetailBinding.titleDetailText.setText(product.getTitle());
            activityDetailBinding.priceDetailText.setText("$" + product.getPrice());
            activityDetailBinding.descriptionDetailText.setText(product.getDescription());

            activityDetailBinding.ratingDetailText.setText(String.valueOf(product.getScore()));
            activityDetailBinding.ratingBar.setRating((float) product.getScore());

            if (user.isFavoriteProduct(product.getProductId())) {
                activityDetailBinding.heartButton.setImageResource(R.drawable.heart);
            } else {
                activityDetailBinding.heartButton.setImageResource(R.drawable.empty_heart);
            }


            activityDetailBinding.heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.isFavoriteProduct(product.getProductId())) {
                        user.removeFavoriteProduct(product.getProductId());
                        activityDetailBinding.heartButton.setImageResource(R.drawable.empty_heart);
                    } else {
                        user.addFavoriteProduct(product.getProductId());
                        activityDetailBinding.heartButton.setImageResource(R.drawable.heart);
                    }
                    updateFavoriteProductsInDatabase(getApplicationContext(), user.getUserId(), user.getFavoriteProducts());
                }
            });

            activityDetailBinding.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.productInCart(product.getProductId())) {
                        int quantity = user.getQuantity(product.getProductId()) + 1;
                        addProductToCartInDatabase(getApplicationContext(), user.getUserId(), product.getProductId(), quantity);
                        user.setItemToCart(product.getProductId(), quantity);
                    } else {
                        addProductToCartInDatabase(getApplicationContext(), user.getUserId(), product.getProductId(), 1);
                        user.setItemToCart(product.getProductId(), 1);
                    }
                }
            });

            activityDetailBinding.backButton.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            });
        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}