package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.techmarket_finalproject.Utilities.DataManager;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.Utilities.SliderItems;
import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.Models.Product;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {
    private ActivityMainBinding activityMainBinding;
    private User user;
    private ArrayList<SliderItems> sliderItems;
    private ArrayList<Category> categories;
    private ArrayList<Product> onSaleProducts;
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
                        activityMainBinding.searchInputField.setText("");
                    }
                    return true;
                }
                return false;
            });

            activityMainBinding.quantityProductsTextView.setText(String.valueOf(user.getCart().size()));

            activityMainBinding.seeAllProductsButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, StoreProductsActivity.class);
                intent.putExtra("category", CategoryEnum.ALL_PRODUCTS);
                startActivity(intent);
                finish();
            });

            AppUtils.statusBarColor(this);

            sliderItems = DataManager.getBannerImages();
            initBannerSlider();

            categories = DataManager.getCategories();
            initCategoryRecyclerView();

            onSaleProducts = new ArrayList<>();
            productsAdapter = new PopularProductsAdapter(onSaleProducts);
            activityMainBinding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            activityMainBinding.recyclerViewProducts.setAdapter(productsAdapter);
            initProductsRecyclerView();

            AppUtils.initNavigationBar(this, activityMainBinding.bottomNavigationBar.getRoot());

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

    private void initCategoryRecyclerView() {
        activityMainBinding.progressBarCategories.setVisibility(TextView.VISIBLE);
        CategoryAdapter categoryAdapter = new CategoryAdapter(MainActivity.this, categories, MainActivity.this);
        activityMainBinding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.recyclerViewCategories.setAdapter(categoryAdapter);
        activityMainBinding.progressBarCategories.setVisibility(TextView.GONE);
    }

    private void initProductsRecyclerView() {
        activityMainBinding.progressBarProducts.setVisibility(TextView.VISIBLE);
        ArrayList<Product> fetchedProducts = ProductManager.getAllProducts();
        onSaleProducts.clear();
        for (Product product : fetchedProducts) {
            if (product.isOnSale()) {
                onSaleProducts.add(product);
            }
        }
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
                onSaleProducts.clear();
//                onSaleProducts.addAll(response);
                for (Product product : response) {
                    if (product.isOnSale()) {
                        onSaleProducts.add(product);
                    }
                    productsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to refresh products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProductList();
        updateCartQuantity();
    }

    private void updateCartQuantity() {
        user = LoginActivity.getCurrentUser();
        activityMainBinding.quantityProductsTextView.setText(String.valueOf(user.getCart().size()));
    }

}