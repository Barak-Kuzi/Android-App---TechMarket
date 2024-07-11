package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Util.ManagmentCart;
import com.example.techmarket_finalproject.databinding.ActivityDetailBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;

public class DetailActivity extends AppCompatActivity {
    
    private ActivityDetailBinding activityDetailBinding;
    private PopularDomain object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        statusBarColor();
        getBundles();
        managmentCart = new ManagmentCart(this);
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.white));
    }

    private void getBundles() {
        object = (PopularDomain) getIntent().getSerializableExtra("object");

        int drawableResourcesId = this.getResources().getIdentifier(object.getImageUrl(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourcesId)
                .into(activityDetailBinding.itemDetailImage);

        activityDetailBinding.titleDetailText.setText(object.getTitle());
        activityDetailBinding.priceDetailText.setText("$" + object.getPrice());
        activityDetailBinding.descriptionDetailText.setText(object.getDescription());
        activityDetailBinding.reviewsDetailText.setText(object.getReview()+"");
        activityDetailBinding.ratingDetailText.setText(object.getScore()+"");

        activityDetailBinding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(numberOrder);
                managmentCart.insertFood(object);
            }
        });

        activityDetailBinding.backButton.setOnClickListener(v -> finish());

    }
}