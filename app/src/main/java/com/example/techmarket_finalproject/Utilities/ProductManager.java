package com.example.techmarket_finalproject.Utilities;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.getAllProductsFromDatabase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.Models.Product;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private static final String TAG = "ProductManager";
    private static final Map<String, Product> productCache = new HashMap<>();
    private static boolean isInitialized = false;

    public static void initialize(Context context, GenericCallBack<ArrayList<Product>> callback) {
        if (!isInitialized) {
            getAllProductsFromDatabase(new GenericCallBack<ArrayList<Product>>() {
                @Override
                public void onResponse(ArrayList<Product> response) {
                    for (Product product : response) {
                        productCache.put(product.getProductId(), product);
                    }
                    isInitialized = true;
                    callback.onResponse(response);
                }

                @Override
                public void onFailure(DatabaseError error) {
                    callback.onFailure(error);
                }
            });
        } else {
            callback.onResponse(new ArrayList<>(productCache.values()));
        }
    }

    public static Product getProductById(String productId) {
        return productCache.get(productId);
    }

    public static ArrayList<Product> getAllProducts() {
        return new ArrayList<>(productCache.values());
    }

    public static ArrayList<Product> getProductsByCategory(Category category) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : productCache.values()) {
            if (product.getCategory() == category) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
}
