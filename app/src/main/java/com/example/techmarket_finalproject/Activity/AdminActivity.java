package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.google.android.material.textfield.TextInputEditText;

public class AdminActivity extends AppCompatActivity {

    private TextView adminPanelLabel;
    private TextInputEditText productIdInput, productNameInput, productPriceInput, discountPercentageInput, productRatingInput, productReviewsInput, productDescriptionInput;
    private AutoCompleteTextView productCategoryDropdown;
    private AppCompatButton addProductButton, cancelAddProductButton;

    private Uri selectedImageUri;
    private String selectedImageUriString;
    private ImageView productImageView;
    private AppCompatButton selectImageButton;
    private static final int IMAGE_SELECTION_REQUEST_CODE = 1;
    private User user;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();
        product = (Product) getIntent().getSerializableExtra("product");

        if (user != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin);

            AppUtils.statusBarColor(this);
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
                    double discountPercentage = discountPercentageInput.getText().toString().isEmpty() ? 0 : Double.parseDouble(discountPercentageInput.getText().toString().trim());
                    boolean isOnSale = discountPercentage > 0;
                    Object imageUri = getImageUri();

                    Product newProduct;
                    if (product != null) {
                        if (imageUri instanceof String) {
                            newProduct = new Product(productId, productName, (String) imageUri, productReviews, productRating, productPrice, productDescription, productCategory, isOnSale, discountPercentage);
                        } else {
                            newProduct = new Product(productId, productName, (int) imageUri, productReviews, productRating, productPrice, productDescription, productCategory, isOnSale, discountPercentage);
                        }
                    } else {
                        newProduct = new Product(productId, productName, selectedImageUri.toString(), productReviews, productRating, productPrice, productDescription, productCategory, isOnSale, discountPercentage);
                    }

                    DatabaseManager.addProductToDatabase(AdminActivity.this, newProduct);
                }
            });

            cancelAddProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product != null) {
                        Intent intent = new Intent(AdminActivity.this, DetailActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                        finish();
                    } else {
//                        startActivity(new Intent(AdminActivity.this, ProfileActivity.class));
                        finish();
                    }
                }
            });

            selectImageButton.setOnClickListener(v -> {
                Intent intent = new Intent(AdminActivity.this, ImageSelectionActivity.class);
                startActivityForResult(intent, IMAGE_SELECTION_REQUEST_CODE);
            });

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String selectedImageUriString = data.getStringExtra("selectedImageUri");
            if (selectedImageUriString != null) {
                selectedImageUri = Uri.parse(selectedImageUriString);
                Glide.with(this).load(selectedImageUri).into(productImageView);
            }
        }
    }

    private void initViews() {
        adminPanelLabel = findViewById(R.id.adding_new_product_label);
        productIdInput = findViewById(R.id.product_id_input);
        productNameInput = findViewById(R.id.product_name_input);
        productPriceInput = findViewById(R.id.product_price_input);
        discountPercentageInput = findViewById(R.id.discount_percentage_input);
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
            discountPercentageInput.setText(String.valueOf(product.getDiscountPercentage()));
            productRatingInput.setText(String.valueOf(product.getScore()));
            productReviewsInput.setText(String.valueOf(product.getReview()));
            productDescriptionInput.setText(product.getDescription());
            productCategoryDropdown.setText(product.getCategory().toString());
            selectedImageUriString = product.getImageUri();
            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                ImageLoader.loadImage(productImageView, product.getImageUri());
            } else {
                ImageLoader.loadImage(productImageView, product.getImageResourceId());
            }
        }
    }

    private boolean hasImageChanged() {
        return selectedImageUri != null && !selectedImageUri.toString().equals(product.getImageUri());
    }

    private Object getImageUri() {
        if (hasImageChanged()) {
            return selectedImageUri.toString();
        } else {
            return product.getImageUri() != null ? product.getImageUri() : product.getImageResourceId();
        }
    }

}