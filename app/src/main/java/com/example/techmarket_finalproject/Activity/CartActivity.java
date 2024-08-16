package com.example.techmarket_finalproject.Activity;

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
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.PaymentService;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityCartBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding activityCartBinding;
    private User user;
    private ArrayList<Product> filteredProducts;
    private double discountPercentage = 0.0;
    private final String[] validCouponCodes = {"SAVE10", "SAVE20", "SAVE50"};
    private double totalCartPrice;

    // For the Stripe API
    private PaymentService paymentService;
    private String PublishableKey = "pk_test_51OKSUWAFluX64oDUOw8ciGfX0NspFHsgHPJbitFIUvz5bMZYEfaErkyWUM95ENoZFqT2oGrHNPsE4H8UQL6Wyt5J00auc6GJZo";
    private String SecretKey = "sk_test_51OKSUWAFluX64oDUMlxnBFHzPUO76MiRgTM8cA1Lo0VwuicOL6pKW4cJxGkFQi44Ngxa5PGUQBeyC9eR6L0tZrHM00lxrk3x4s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = LoginActivity.getCurrentUser();

        if (user != null) {

            activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
            setContentView(activityCartBinding.getRoot());

            AppUtils.statusBarColor(this);
            initList();
            setupCouponButton();

            AppUtils.initNavigationBar(this, activityCartBinding.bottomNavigationBar.getRoot());
            activityCartBinding.bottomNavigationBar.getRoot().setSelectedItemId(R.id.menu_cart);

            paymentService = new PaymentService(this, PublishableKey, SecretKey);

            activityCartBinding.checkoutButton.setOnClickListener(v -> {
                Toast.makeText(this, "Loading Payment System...", Toast.LENGTH_SHORT).show();
                paymentService.createCustomer(totalCartPrice);
            });


        } else {
            Toast.makeText(this, "The Page is Loading...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initList() {
        if (user.cartIsEmpty()) {
            activityCartBinding.emptyCartText.setVisibility(View.VISIBLE);
            activityCartBinding.cartView.setVisibility(View.GONE);
            activityCartBinding.checkoutButton.setEnabled(false);
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

    public void savePurchaseToHistory() {
        HashMap<String, Object> purchase = new HashMap<>();
        HashMap<String, Integer> products = new HashMap<>();
        for (Product product : filteredProducts) {
            products.put(product.getProductId(), user.getCart().get(product.getProductId()));
        }
        purchase.put("products", products);
        purchase.put("totalPrice", totalCartPrice);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        purchase.put("purchaseDate", currentDate);

        user.addPurchaseToHistory(purchase);
        DatabaseManager.savePurchaseToHistory(this, user.getUserId(), user.getPurchaseHistory().size() - 1, purchase);
    }

}