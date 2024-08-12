package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techmarket_finalproject.Adapters.CartAdapter;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;

import com.example.techmarket_finalproject.Utilities.AppUtils;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ActivityCartBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding activityCartBinding;
    private User user;
    private ArrayList<Product> filteredProducts;
    private double discountPercentage = 0.0;
    private final String[] validCouponCodes = {"SAVE10", "SAVE20", "SAVE50"};
    private double totalCartPrice;

    // For the Stripe API
    private String PublishableKey = "pk_test_51OKSUWAFluX64oDUOw8ciGfX0NspFHsgHPJbitFIUvz5bMZYEfaErkyWUM95ENoZFqT2oGrHNPsE4H8UQL6Wyt5J00auc6GJZo";
    private String SecretKey = "sk_test_51OKSUWAFluX64oDUMlxnBFHzPUO76MiRgTM8cA1Lo0VwuicOL6pKW4cJxGkFQi44Ngxa5PGUQBeyC9eR6L0tZrHM00lxrk3x4s";
    private String CustomerId;
    private String EphemeralKey;
    private String ClientSecret;
    private PaymentSheet paymentSheet;

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

            PaymentConfiguration.init(getApplicationContext(), PublishableKey);

            paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
                onPaymentResult(paymentSheetResult);
            });

            activityCartBinding.checkoutButton.setOnClickListener(v -> {
                Toast.makeText(this, "Loading Payment System...", Toast.LENGTH_SHORT).show();
                createCustomer(totalCartPrice);
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

    private void savePurchaseToHistory() {
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

    // For the Stripe API
    private void createCustomer(double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomerId = object.getString("id");
                    getEphemeralKey(totalAmount);
                } catch (JSONException e) {
                    Log.e("PaymentActivity", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentActivity", "Volley Error: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            savePurchaseToHistory();
            user.clearCart();
            DatabaseManager.clearCartInDatabase(this, user.getUserId());
            startActivity(new Intent(CartActivity.this, MainActivity.class));
            finish();
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e("PaymentActivity", "Payment Failed: " + failedResult.getError().getMessage());
            Toast.makeText(this, "Payment Failed: " + failedResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphemeralKey(double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    EphemeralKey = object.getString("id");
                    getClientSecret(CustomerId, EphemeralKey, totalAmount);

                } catch (JSONException e) {
                    Log.e("PaymentActivity", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentActivity", "Volley Error: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                headers.put("Stripe-Version", "2024-06-20");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerId, String ephemeralKey, double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ClientSecret = object.getString("client_secret");
                    paymentFlow();
                } catch (JSONException e) {
                    Log.e("PaymentActivity", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentActivity", "Volley Error: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", String.valueOf((int) (totalAmount * 100)));
                params.put("currency", "usd");
                params.put("customer", customerId);
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}