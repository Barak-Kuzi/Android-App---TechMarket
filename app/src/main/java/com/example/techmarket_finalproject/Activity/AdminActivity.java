package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addAllProductsToDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private TextView adminPanelLabel;
    private TextInputEditText productIdInput, productNameInput, productPriceInput, productRatingInput, productReviewsInput, productDescriptionInput;
    private AutoCompleteTextView productCategoryDropdown;
    private AppCompatButton addProductButton, cancelAddProductButton;

    Uri selectedImageUri;
    private ImageView productImageView;
    private AppCompatButton selectImageButton;
    private static final int IMAGE_SELECTION_REQUEST_CODE = 1;
    private User user;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");
        product = (Product) getIntent().getSerializableExtra("product");

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin);

            statusBarColor();
            initViews();
            setupCategorySpinner();
            editOrAddProduct();

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

                    Product newProduct = new Product(productId, productName, selectedImageUri.toString(), productReviews, productRating, productPrice, productDescription, productCategory);

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

            selectImageButton.setOnClickListener(v -> {
                Intent intent = new Intent(AdminActivity.this, ImageSelectionActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, IMAGE_SELECTION_REQUEST_CODE);
            });

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void statusBarColor() {
        Window window = AdminActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AdminActivity.this, R.color.new_green));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String selectedImageUriString = data.getStringExtra("selectedImageUri");
            if (selectedImageUriString != null) {
                selectedImageUri = Uri.parse(selectedImageUriString);
                Log.d("AdminActivity", "Selected image URI: " + selectedImageUri);
                Glide.with(this).load(selectedImageUri).into(productImageView);
            }
        }
    }

    private void initViews() {
        adminPanelLabel = findViewById(R.id.adding_new_product_label);
        productIdInput = findViewById(R.id.product_id_input);
        productNameInput = findViewById(R.id.product_name_input);
        productPriceInput = findViewById(R.id.product_price_input);
        productRatingInput = findViewById(R.id.product_rating_input);
        productReviewsInput = findViewById(R.id.product_review_input);
        productDescriptionInput = findViewById(R.id.product_description_input);
        productCategoryDropdown = findViewById(R.id.product_category_dropdown);
        addProductButton = findViewById(R.id.add_product_button);
        cancelAddProductButton = findViewById(R.id.cancel_add_product_button);
        productImageView = findViewById(R.id.product_image_view);
        selectImageButton = findViewById(R.id.select_image_button);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CategoryEnum> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, CategoryEnum.values());
        productCategoryDropdown.setAdapter(adapter);
    }

    private void editOrAddProduct() {
        if (product != null) {
            adminPanelLabel.setText("Edit Product");
            addProductButton.setText("Update Product");
            productIdInput.setText(product.getProductId());
            productNameInput.setText(product.getTitle());
            productPriceInput.setText(String.valueOf(product.getPrice()));
            productRatingInput.setText(String.valueOf(product.getScore()));
            productReviewsInput.setText(String.valueOf(product.getReview()));
            productDescriptionInput.setText(product.getDescription());
            productCategoryDropdown.setText(product.getCategory().toString());
            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                ImageLoader.loadImage(productImageView, product.getImageUri());
            } else {
                ImageLoader.loadImage(productImageView, product.getImageResourceId());
            }
        }
    }

}