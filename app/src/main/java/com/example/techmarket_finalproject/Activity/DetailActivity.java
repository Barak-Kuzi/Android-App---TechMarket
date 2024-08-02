package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addProductToCartOfDB;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
//import com.example.techmarket_finalproject.Utilities.ManagmentCart;
import com.example.techmarket_finalproject.databinding.ActivityDetailBinding;
import com.example.techmarket_finalproject.Models.Product;

public class DetailActivity extends AppCompatActivity {
    
    private ActivityDetailBinding activityDetailBinding;
    private Product product;
    private User user;
    private int numberOrder = 1;
//    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        statusBarColor();
        getBundles();
//        managmentCart = new ManagmentCart(this);
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.purple_Dark));
    }

    private void getBundles() {
        this.product = (Product) getIntent().getSerializableExtra("product");
        this.user = (User) getIntent().getSerializableExtra("user");

//        int drawableResourcesId = this.getResources().getIdentifier(object.getImageResourceId(), "drawable", this.getPackageName());

        Glide.with(this)
                .load(product.getImageResourceId())
                .into(activityDetailBinding.itemDetailImage);

        activityDetailBinding.titleDetailText.setText(product.getTitle());
        activityDetailBinding.priceDetailText.setText("$" + product.getPrice());
        activityDetailBinding.descriptionDetailText.setText(product.getDescription());
        activityDetailBinding.reviewsDetailText.setText(String.valueOf(product.getReview()));
        activityDetailBinding.ratingDetailText.setText(String.valueOf(product.getScore()));

        activityDetailBinding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                product.setNumberInCart(numberOrder);
//                managmentCart.insertFood(product);
                if (user.productInCart(product.getProductId())) {
                    int quantity = user.getQuantity(product.getProductId()) + 1;
                    addProductToCartOfDB(getApplicationContext(), user.getUserId(), product.getProductId(), quantity, user);
                    user.setItemToCart(product.getProductId(), quantity);
                } else {
                    addProductToCartOfDB(getApplicationContext(), user.getUserId(), product.getProductId(), 1, user);
                    user.setItemToCart(product.getProductId(), 1);
                }
            }
        });

        activityDetailBinding.backButton.setOnClickListener(v ->{
            Log.d("cart", user.getCart().toString());
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });
    }
}