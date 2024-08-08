package com.example.techmarket_finalproject.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.techmarket_finalproject.Activity.CartActivity;
import com.example.techmarket_finalproject.Activity.FavoriteProductsActivity;
import com.example.techmarket_finalproject.Activity.MainActivity;
import com.example.techmarket_finalproject.Activity.ProfileActivity;
import com.example.techmarket_finalproject.Activity.StoreProductsActivity;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppUtils {

    public static void initNavigationBar(@NonNull Activity activity, @NonNull BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Intent intent = null;
            Class<?> targetActivity = null;

            if (itemId == R.id.menu_home) {
                targetActivity = MainActivity.class;
            } else if (itemId == R.id.menu_browse) {
                targetActivity = StoreProductsActivity.class;
                intent = new Intent(activity, targetActivity);
                intent.putExtra("category", CategoryEnum.ALL_PRODUCTS);
            } else if (itemId == R.id.menu_cart) {
                targetActivity = CartActivity.class;
            } else if (itemId == R.id.menu_favorites) {
                targetActivity = FavoriteProductsActivity.class;
            } else if (itemId == R.id.menu_profile) {
                targetActivity = ProfileActivity.class;
            } else {
                return false;
            }

            if (targetActivity != null && !activity.getClass().equals(targetActivity)) {
                if (intent == null) {
                    intent = new Intent(activity, targetActivity);
                }
                activity.startActivity(intent);
                activity.finish();
            }
            return true;
        });
    }

    public static void statusBarColor(@NonNull Activity activity) {
        Window window = activity.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.new_green));
    }
}