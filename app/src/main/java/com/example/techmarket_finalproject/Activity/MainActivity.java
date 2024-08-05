package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.techmarket_finalproject.Adapters.CategoryAdapter;
import com.example.techmarket_finalproject.Adapters.PopularProductsAdapter;
import com.example.techmarket_finalproject.Adapters.SliderAdapter;
import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;

import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.Utilities.SliderItems;
import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.Models.Product;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {
    ActivityMainBinding activityMainBinding;
    private TextView welcomeUser;

    private User user;
    ArrayList<SliderItems> sliderItems;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(activityMainBinding.getRoot());

//            welcomeUser = findViewById(R.id.welcome_user_name);
//            welcomeUser.setText(user.getName());

            statusBarColor();

            sliderItems = new ArrayList<>();
            initBannerSlider();

            categories = new ArrayList<>();
            initCategoryRecyclerView();

            initProductsRecyclerView();
//            initCategoryButtons(user);
            bottomNavigation();

            // Initialize database with products if not already done
            DatabaseManager.initializeDatabaseWithProducts(this);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initBannerSlider() {
        activityMainBinding.progressBarBanners.setVisibility(TextView.VISIBLE);
        DatabaseManager.setBannerSliderImages(this, new GenericCallBack<ArrayList<SliderItems>>() {
            @Override
            public void onResponse(ArrayList<SliderItems> response) {
                sliderItems = response;
                SliderAdapter sliderAdapter = new SliderAdapter(MainActivity.this, sliderItems, activityMainBinding.viewPagerSlider);
                activityMainBinding.viewPagerSlider.setAdapter(sliderAdapter);
                activityMainBinding.viewPagerSlider.setClipToPadding(false);
                activityMainBinding.viewPagerSlider.setClipChildren(false);
                activityMainBinding.viewPagerSlider.setOffscreenPageLimit(3);
                activityMainBinding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));

                activityMainBinding.viewPagerSlider.setPageTransformer(compositePageTransformer);
                activityMainBinding.progressBarBanners.setVisibility(TextView.GONE);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load banner images.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCategoryRecyclerView() {
        activityMainBinding.progressBarCategories.setVisibility(TextView.VISIBLE);
        DatabaseManager.getAllCategoriesFromDatabase(new GenericCallBack<ArrayList<Category>>() {
            @Override
            public void onResponse(ArrayList<Category> response) {
                categories.clear();
                categories.addAll(response);
                CategoryAdapter categoryAdapter = new CategoryAdapter(MainActivity.this, response, MainActivity.this);
                activityMainBinding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                activityMainBinding.recyclerViewCategories.setAdapter(categoryAdapter);
                activityMainBinding.progressBarCategories.setVisibility(TextView.GONE);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCategoryButtons(User user) {
//        activityMainBinding.phonesCategoryButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);
//            intent.putExtra("user", user);
//            intent.putExtra("category", Category.CELL_PHONES);
//            startActivity(intent);
//            finish();
//        });
//
//        activityMainBinding.viewAllProducts.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);
//            intent.putExtra("user", user);
//            intent.putExtra("category", Category.ALL_PRODUCTS);
//            startActivity(intent);
//            finish();
//        });
    }

    private void bottomNavigation() {
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
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.new_green));
    }

    private void initProductsRecyclerView() {
        activityMainBinding.progressBarProducts.setVisibility(TextView.VISIBLE);
        ArrayList<Product> products = ProductManager.getAllProducts();

//        activityMainBinding.popularProductsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        activityMainBinding.popularProductsView.setAdapter(new PopularProductsAdapter(products, user));
        activityMainBinding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        activityMainBinding.recyclerViewProducts.setAdapter(new PopularProductsAdapter(products, user));
        activityMainBinding.progressBarProducts.setVisibility(TextView.GONE);
    }


    @Override
    public void onCategoryClick(int position) {
        if (categories != null && !categories.isEmpty() && position >= 0 && position < categories.size()) {
            Category clickedCategory = categories.get(position);
            Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);

            if (clickedCategory.getTitle().equals("Phones")) {
                intent.putExtra("user", user);
                intent.putExtra("category", CategoryEnum.CELL_PHONES);
//                startActivity(intent);
//                return;
            }

            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid category selected.", Toast.LENGTH_SHORT).show();
        }
    }
}