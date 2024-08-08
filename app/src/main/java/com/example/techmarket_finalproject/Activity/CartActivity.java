package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapters.CartAdapter;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;

import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding activityCartBinding;
    private User user;
    private ArrayList<Product> filteredProducts;
    private double discountPercentage = 0.0;
    private final String[] validCouponCodes = {"SAVE10", "SAVE20", "SAVE50"};
    private double totalCartPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();

        if (user != null) {

            activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
            setContentView(activityCartBinding.getRoot());

            AppUtils.statusBarColor(this);
            setButton();
            initList();
            setupCouponButton();

            AppUtils.initNavigationBar(this, activityCartBinding.bottomNavigationBar.getRoot());
            activityCartBinding.bottomNavigationBar.getRoot().setSelectedItemId(R.id.menu_cart);

        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initList() {
        if (user.cartIsEmpty()) {
            activityCartBinding.emptyCartText.setVisibility(View.VISIBLE);
            activityCartBinding.cartView.setVisibility(View.GONE);
        } else {
            activityCartBinding.emptyCartText.setVisibility(View.GONE);
            activityCartBinding.cartView.setVisibility(View.VISIBLE);
        }

        filteredProducts = new ArrayList<>();
        filterProductsByKeys(user.getCart().keySet());
        initRecyclerView();
        calculateTotalPrice();
    }

    private void filterProductsByKeys(Set<String> keys) {
        for (String key : keys) {
            Product product = ProductManager.getProductById(key);
            if (product != null) {
                filteredProducts.add(product);
            }
        }
    }

    private void initRecyclerView() {
        activityCartBinding.cartView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        activityCartBinding.cartView.setAdapter(new CartAdapter(filteredProducts, user, user.getCart(), new UpdateQuantityProductsListener() {
            @Override
            public void update() {
                calculateTotalPrice();
            }
        }));
    }

    private void calculateTotalPrice() {
        double percentTax = 0.02;
        double subtotal = user.getTotalPrice(filteredProducts);
        double deliveryPrice = user.cartIsEmpty() ? 0 : 10;
        double discount = subtotal * discountPercentage;
        totalCartPrice = (subtotal - discount) + deliveryPrice;


        String formattedSubtotalPrice = String.format("%.2f", subtotal);
        String formattedTotalCartPrice = String.format("%.2f", totalCartPrice);
        String buttonText = "Checkout for <b>$" + formattedTotalCartPrice + "</b>";

        activityCartBinding.subtotalText.setText("$" + formattedSubtotalPrice);
        activityCartBinding.deliveryText.setText("$" + deliveryPrice);
        activityCartBinding.discountText.setText((discountPercentage * 100) + "%");
        activityCartBinding.checkoutButton.setText(HtmlCompat.fromHtml(buttonText, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setupCouponButton() {
        activityCartBinding.couponEditTextLayout.findViewById(R.id.apply_coupon_button).setOnClickListener(v -> {
            String enteredCoupon = activityCartBinding.couponEditText.getText().toString().trim();
            if (isValidCoupon(enteredCoupon)) {
                applyDiscount(enteredCoupon);
                calculateTotalPrice();
                activityCartBinding.couponEditTextLayout.setVisibility(View.GONE);
                activityCartBinding.couponConfirmLayout.setVisibility(View.VISIBLE);
                activityCartBinding.couponCodeText.setText(enteredCoupon);
                Toast.makeText(this, "Coupon applied successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid coupon code.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidCoupon(String coupon) {
        for (String validCoupon : validCouponCodes) {
            if (validCoupon.equalsIgnoreCase(coupon)) {
                return true;
            }
        }
        return false;
    }

    private void applyDiscount(String coupon) {
        switch (coupon.toUpperCase()) {
            case "SAVE10":
                discountPercentage = 0.10; // 10% discount
                break;
            case "SAVE20":
                discountPercentage = 0.20; // 20% discount
                break;
            case "SAVE50":
                discountPercentage = 0.50; // 50% discount
                break;
            default:
                discountPercentage = 0.0;
                break;
        }
    }

    private void setButton() {
//        activityCartBinding.backButtonViewCart.setOnClickListener(v -> {
//            finish();
//        });

        activityCartBinding.checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("totalAmount", totalCartPrice);
            startActivity(intent);
        });
    }

}