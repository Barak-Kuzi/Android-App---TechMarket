package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.ProductAdapter;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityStoreProductsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StoreProductsActivity extends AppCompatActivity {
    ActivityStoreProductsBinding activityStoreProductsBinding;
    private ArrayList<Product> productList;
    private ProductAdapter productAdapter;
    private AppCompatButton lastClickedButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");
        CategoryEnum category = (CategoryEnum) getIntent().getSerializableExtra("category");

        if (user != null) {
            productList = new ArrayList<>();
            statusBarColor();
            initViews(user);
            setupCategoryButtons();
            initBottomNavigationBar();

            if (category != null) {
                selectCategory(category);
            }

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void selectCategory(CategoryEnum category) {
        AppCompatButton buttonToClick = null;
        switch (category) {
            case ALL_PRODUCTS:
                buttonToClick = activityStoreProductsBinding.allCategoryButton;
                break;
            case CELL_PHONES:
                buttonToClick = activityStoreProductsBinding.cpCategoryButton;
                break;
            case HEADPHONES:
                buttonToClick = activityStoreProductsBinding.hpCategoryButton;
                break;
            case SMART_WATCHES:
                buttonToClick = activityStoreProductsBinding.swCategoryButton;
                break;
            case LAPTOPS:
                buttonToClick = activityStoreProductsBinding.lpCategoryButton;
                break;
            case DESKTOPS:
                buttonToClick = activityStoreProductsBinding.dtCategoryButton;
                break;
            case TELEVISIONS:
                buttonToClick = activityStoreProductsBinding.tvCategoryButton;
                break;

        }
        if (buttonToClick != null) {
            buttonToClick.performClick();
        }
    }

    private void statusBarColor() {
        Window window = StoreProductsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(StoreProductsActivity.this, R.color.new_green));
    }

    private void initViews(User user) {
        activityStoreProductsBinding = ActivityStoreProductsBinding.inflate(getLayoutInflater());
        setContentView(activityStoreProductsBinding.getRoot());

//        activityStoreProductsBinding.backButtonToHomePage.setOnClickListener(v -> {
//            Intent intent = new Intent(StoreProductsActivity.this, MainActivity.class);
//            intent.putExtra("user", user);
//            startActivity(intent);
//            finish();
//        });

        activityStoreProductsBinding.cartView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, user);
        activityStoreProductsBinding.cartView.setAdapter(productAdapter);

        EditText searchField = findViewById(R.id.edit_text_search_field_store_products);
        searchField.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                String query = searchField.getText().toString();
                Intent intent = new Intent(StoreProductsActivity.this, SearchActivity.class);
                intent.putExtra("query", query);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void setupCategoryButtons() {
        activityStoreProductsBinding.allCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.allCategoryButton, CategoryEnum.ALL_PRODUCTS));
        activityStoreProductsBinding.cpCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.cpCategoryButton, CategoryEnum.CELL_PHONES));
        activityStoreProductsBinding.hpCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.hpCategoryButton, CategoryEnum.HEADPHONES));
        activityStoreProductsBinding.swCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.swCategoryButton, CategoryEnum.SMART_WATCHES));
        activityStoreProductsBinding.lpCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.lpCategoryButton, CategoryEnum.LAPTOPS));
        activityStoreProductsBinding.dtCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.dtCategoryButton, CategoryEnum.DESKTOPS));
        activityStoreProductsBinding.tvCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.tvCategoryButton, CategoryEnum.TELEVISIONS));
    }

    private void onCategoryButtonClick(AppCompatButton clickedButton, CategoryEnum category) {
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundResource(R.drawable.category_button_background);
        }
        clickedButton.setBackgroundResource(R.drawable.category_button_selected_background);
        lastClickedButton = clickedButton;
        fetchProductsByCategory(category);
    }

    private void fetchProductsByCategory(CategoryEnum category) {
        ArrayList<Product> products = new ArrayList<>();
        if (category.toString().equals("ALL_PRODUCTS")) {
            products = ProductManager.getAllProducts();
        } else {
            products = ProductManager.getProductsByCategory(category);
        }
        productList.clear();
        productList.addAll(products);
        productAdapter.notifyDataSetChanged();
    }

    private void initBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                Intent intent = new Intent(StoreProductsActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_browse) {
                return true;
            } else if (itemId == R.id.menu_cart) {
                Intent intent = new Intent(StoreProductsActivity.this, CartActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_favorites) {
                Intent intent = new Intent(StoreProductsActivity.this, FavoriteProductsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent intent = new Intent(StoreProductsActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                return true;
            } else {
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_browse);
    }

}