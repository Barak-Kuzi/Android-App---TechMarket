package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Adapters.ProductAdapter;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivitySearchBinding;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding activitySearchBinding;
//    private RecyclerView searchResultsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> searchResults;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();
        String query = getIntent().getStringExtra("query");

        if (user != null && query != null) {
            activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
            setContentView(activitySearchBinding.getRoot());

            String foundProducts = "Found <b>" + ProductManager.searchProductsByName(query).size() + "</b> products";

            activitySearchBinding.editTextSearchFieldStoreProducts.setText(query);
            activitySearchBinding.resultForText.setText("Results for " + "\"" + query + "\"");
            activitySearchBinding.amountFoundProducts.setText(HtmlCompat.fromHtml(foundProducts, HtmlCompat.FROM_HTML_MODE_LEGACY));

            activitySearchBinding.backButtonToHomePage.setOnClickListener(v -> {
                finish();
            });



            activitySearchBinding.searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            searchResults = ProductManager.searchProductsByName(query);
            productAdapter = new ProductAdapter(searchResults, user);
            activitySearchBinding.searchResultRecyclerView.setAdapter(productAdapter);

            activitySearchBinding.editTextSearchFieldStoreProducts.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    performSearch(activitySearchBinding.editTextSearchFieldStoreProducts.getText().toString());
                    return true;
                }
                return false;
            });
        } else {
            Toast.makeText(this, "The page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void performSearch(String query) {
        searchResults = ProductManager.searchProductsByName(query);
        productAdapter.updateData(searchResults);
        activitySearchBinding.resultForText.setText("Results for " + "\"" + query + "\"");
        activitySearchBinding.amountFoundProducts.setText("Found " + searchResults.size() + " products");
    }


}