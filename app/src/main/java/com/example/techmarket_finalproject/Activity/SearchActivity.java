package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Adapters.ProductAdapter;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ProductManager;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> searchResults;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        user = (User) getIntent().getSerializableExtra("user");
        String query = getIntent().getStringExtra("query");

        if (user != null && query != null) {
            searchResultsRecyclerView = findViewById(R.id.cartView);
            searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            searchResults = ProductManager.searchProductsByName(query);
            Log.d("SearchActivity", "searchResults: " + searchResults);
            productAdapter = new ProductAdapter(searchResults, user);
            searchResultsRecyclerView.setAdapter(productAdapter);
        } else {
            Toast.makeText(this, "The page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}