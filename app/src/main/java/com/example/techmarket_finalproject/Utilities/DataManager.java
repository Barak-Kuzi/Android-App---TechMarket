package com.example.techmarket_finalproject.Utilities;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addAllProductsToDatabase;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.R;

import java.util.ArrayList;

public class DataManager {

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();

        allProducts.add(new Product("P1", "T-shirt black", R.drawable.item_1, 15, 4, 500, "Description for T-shirt black"));
        allProducts.add(new Product("P2", "Smart Watch", R.drawable.item_2, 10, 4.5, 450, "Description for Smart Watch"));
        allProducts.add(new Product("P3", "Phone", R.drawable.item_3, 3, 4.9, 800, "Description for Phone"));
        allProducts.add(new Product("P4", "Phone", R.drawable.b_1, 3, 4.9, 800, "Description for Phone"));

        return allProducts;
    }




}
