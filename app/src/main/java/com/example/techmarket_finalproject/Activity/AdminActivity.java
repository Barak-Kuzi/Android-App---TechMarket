package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addAllProductsToDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private TextInputEditText productIdInput, productNameInput, productPriceInput, productRatingInput, productReviewsInput, productDescriptionInput;
    private AppCompatButton addProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin);

            initViews();

            addProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = productIdInput.getText().toString().trim();
                    String productName = productNameInput.getText().toString().trim();
                    double productPrice = Double.parseDouble(productPriceInput.getText().toString().trim());
                    double productRating = Double.parseDouble(productRatingInput.getText().toString().trim());
                    int productReviews = Integer.parseInt(productReviewsInput.getText().toString().trim());
                    String productDescription = productDescriptionInput.getText().toString().trim();

                    Product newProduct = new Product(productId, productName, R.drawable.default_image, productReviews, productRating, productPrice, productDescription);
                    ArrayList<Product> products = new ArrayList<>();
                    products.add(newProduct);

                    addAllProductsToDatabase(AdminActivity.this, products);
                    Toast.makeText(AdminActivity.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                }
            });

            Log.d("ProfileActivity", "User: " + user.isAdmin());
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
        addProductButton = findViewById(R.id.add_product_button);
    }
}