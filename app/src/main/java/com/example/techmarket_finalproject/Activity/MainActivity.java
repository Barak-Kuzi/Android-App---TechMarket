package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.techmarket_finalproject.Utilities.AppUtils;
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
    private ArrayList<Product> products;
    private PopularProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();


        if (user != null) {

            activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(activityMainBinding.getRoot());

            activityMainBinding.userNameTextView.setText(user.getName());
            activityMainBinding.shoppingBagLayout.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
            });

            activityMainBinding.searchInputField.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String query = activityMainBinding.searchInputField.getText().toString().trim();
                    if (!query.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("query", query);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            });

            AppUtils.statusBarColor(this);

            sliderItems = new ArrayList<>();
            initBannerSlider();

            categories = new ArrayList<>();
            initCategoryRecyclerView();

            products = new ArrayList<>();
            productsAdapter = new PopularProductsAdapter(products);
            activityMainBinding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            activityMainBinding.recyclerViewProducts.setAdapter(productsAdapter);
            initProductsRecyclerView();

            AppUtils.initNavigationBar(this, activityMainBinding.bottomNavigationBar.getRoot());

            // Initialize database with products if not already done
            DatabaseManager.initializeDatabaseWithProducts(this);

            // Refresh products list
            if (ProductManager.isProductDeleted()) {
                refreshProductList();
                ProductManager.setProductDeleted(false);
            }

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

    private void initProductsRecyclerView() {
        activityMainBinding.progressBarProducts.setVisibility(TextView.VISIBLE);
        ArrayList<Product> fetchedProducts = ProductManager.getAllProducts();
        products.clear();
        products.addAll(fetchedProducts);
        productsAdapter.notifyDataSetChanged();
        activityMainBinding.progressBarProducts.setVisibility(TextView.GONE);
    }

    @Override
    public void onCategoryClick(int position) {
        if (categories != null && !categories.isEmpty() && position >= 0 && position < categories.size()) {
            Category clickedCategory = categories.get(position);
            Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);

            switch (clickedCategory.getTitle()) {
                case "Phones":
                    intent.putExtra("category", CategoryEnum.CELL_PHONES);
                    break;
                case "Smartwatches":
                    intent.putExtra("category", CategoryEnum.SMART_WATCHES);
                    break;
                case "Laptops":
                    intent.putExtra("category", CategoryEnum.LAPTOPS);
                    break;
                case "Headphones":
                    intent.putExtra("category", CategoryEnum.HEADPHONES);
                    break;
                case "Desktops":
                    intent.putExtra("category", CategoryEnum.DESKTOPS);
                    break;
                case "Televisions":
                    intent.putExtra("category", CategoryEnum.TELEVISIONS);
                    break;
                case "See all":
                    intent.putExtra("category", CategoryEnum.ALL_PRODUCTS);
                    break;
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid category selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshProductList() {
        DatabaseManager.getAllProductsFromDatabase(new GenericCallBack<ArrayList<Product>>() {
            @Override
            public void onResponse(ArrayList<Product> response) {
                products.clear();
                products.addAll(response);
                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to refresh products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}