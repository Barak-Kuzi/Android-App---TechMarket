package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addAllProductsToDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private TextInputEditText productIdInput, productNameInput, productPriceInput, productRatingInput, productReviewsInput, productDescriptionInput;
    private AutoCompleteTextView productCategoryDropdown;
    private AppCompatButton addProductButton, cancelAddProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin);

            initViews();
            setupCategorySpinner();

            addProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = productIdInput.getText().toString().trim();
                    String productName = productNameInput.getText().toString().trim();
                    double productPrice = Double.parseDouble(productPriceInput.getText().toString().trim());
                    double productRating = Double.parseDouble(productRatingInput.getText().toString().trim());
                    int productReviews = Integer.parseInt(productReviewsInput.getText().toString().trim());
                    String productDescription = productDescriptionInput.getText().toString().trim();
                    CategoryEnum productCategory = CategoryEnum.valueOf(productCategoryDropdown.getText().toString().trim());

                    Product newProduct = new Product(productId, productName, R.drawable.default_image, productReviews, productRating, productPrice, productDescription, productCategory);

                    // Add product to the database
                    ArrayList<Product> products = new ArrayList<>();
                    products.add(newProduct);
                    addAllProductsToDatabase(AdminActivity.this, products);

                    Toast.makeText(AdminActivity.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                }
            });

            cancelAddProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminActivity.this, ProfileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            });


        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initViews() {
        productIdInput = findViewById(R.id.product_id_input);
        productNameInput = findViewById(R.id.product_name_input);
        productPriceInput = findViewById(R.id.product_price_input);
        productRatingInput = findViewById(R.id.product_rating_input);
        productReviewsInput = findViewById(R.id.product_review_input);
        productDescriptionInput = findViewById(R.id.product_description_input);
        productCategoryDropdown = findViewById(R.id.product_category_dropdown);
        addProductButton = findViewById(R.id.add_product_button);
        cancelAddProductButton = findViewById(R.id.cancel_add_product_button);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CategoryEnum> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, CategoryEnum.values());
        productCategoryDropdown.setAdapter(adapter);
    }
}