package com.example.techmarket_finalproject.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Adapters.ProductAdapter;
import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityFavoriteProductsBinding;
import com.example.techmarket_finalproject.databinding.ActivityStoreProductsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreProductsActivity extends AppCompatActivity {
    ActivityStoreProductsBinding activityStoreProductsBinding;
    private ArrayList<Product> productList;
    private ProductAdapter productAdapter;
    private AppCompatButton lastClickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            productList = new ArrayList<>();
            statusBarColor();
            initViews(user);
            setupCategoryButtons();
        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void statusBarColor() {
        Window window = StoreProductsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(StoreProductsActivity.this, R.color.purple_Dark));
    }

    private void initViews(User user) {
        activityStoreProductsBinding = ActivityStoreProductsBinding.inflate(getLayoutInflater());
        setContentView(activityStoreProductsBinding.getRoot());

        activityStoreProductsBinding.backButtonToHomePage.setOnClickListener(v -> {
            Intent intent = new Intent(StoreProductsActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        activityStoreProductsBinding.cartView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, user);
        activityStoreProductsBinding.cartView.setAdapter(productAdapter);
    }

    private void setupCategoryButtons() {
//        activityStoreProductsBinding.allCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.allCategoryButton, Category.ALL));
        activityStoreProductsBinding.swCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.swCategoryButton, Category.SMART_WATCHES));
        activityStoreProductsBinding.cpCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.cpCategoryButton, Category.CELL_PHONES));
        activityStoreProductsBinding.lpCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.lpCategoryButton, Category.LAPTOPS));
        activityStoreProductsBinding.tvCategoryButton.setOnClickListener(v -> onCategoryButtonClick(activityStoreProductsBinding.tvCategoryButton, Category.TELEVISIONS));
    }


    private void onCategoryButtonClick(AppCompatButton clickedButton, Category category) {
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundResource(R.drawable.category_button_background);
        }
        clickedButton.setBackgroundResource(R.drawable.continue_btn_background);
        lastClickedButton = clickedButton;
        fetchProductsByCategory(category);
    }

//    private void fetchProductsByCategory(Category category) {
//        DatabaseManager.getProductsByCategory(category, new GenericCallBack<ArrayList<Product>>() {
//            @Override
//            public void onResponse(ArrayList<Product> products) {
//                productList.clear();
//                productList.addAll(products);
//                productAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(DatabaseError databaseError) {
//                Toast.makeText(StoreProductsActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void fetchProductsByCategory(Category category) {
        ArrayList<Product> products = ProductManager.getProductsByCategory(category);
        productList.clear();
        productList.addAll(products);
        productAdapter.notifyDataSetChanged();
    }
}