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
    private ProductAdapter productAdapter;
    private List<Product> searchResults;
    private User user;
    String query;
    private int amountFoundProducts;
    private String foundProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();
        query = getIntent().getStringExtra("query");

        if (user != null && query != null) {
            activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
            setContentView(activitySearchBinding.getRoot());

            amountFoundProducts = ProductManager.searchProductsByName(query).size();

            activitySearchBinding.editTextSearchFieldStoreProducts.setText(query);
            setTextResult(query, amountFoundProducts);

            activitySearchBinding.backButtonSearch.setOnClickListener(v -> {
                finish();
            });


            activitySearchBinding.searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            searchResults = ProductManager.searchProductsByName(query);
            productAdapter = new ProductAdapter(searchResults);
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
        setTextResult(query, searchResults.size());
    }

    private void setTextResult(String query, int amountFoundProducts) {
        activitySearchBinding.resultForText.setText("Results for " + "\"" + query + "\"");
        if (amountFoundProducts > 0) {
            foundProducts = "Found <b>" + amountFoundProducts + "</b> products";
        } else {
            foundProducts = "Not found products";
        }
        activitySearchBinding.amountFoundProducts.setText(HtmlCompat.fromHtml(foundProducts, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

}