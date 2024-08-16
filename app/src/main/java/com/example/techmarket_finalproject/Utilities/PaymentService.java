package com.example.techmarket_finalproject.Utilities;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techmarket_finalproject.Activity.CartActivity;
import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Activity.MainActivity;
import com.example.techmarket_finalproject.Models.User;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    private final CartActivity cartActivity;
    private final String publishableKey;
    private final String secretKey;
    private PaymentSheet paymentSheet;
    private String customerId;
    private String ephemeralKey;
    private String clientSecret;

    public PaymentService(CartActivity cartActivity, String publishableKey, String secretKey) {
        this.cartActivity = cartActivity;
        this.publishableKey = publishableKey;
        this.secretKey = secretKey;
        PaymentConfiguration.init(cartActivity.getApplicationContext(), publishableKey);
        this.paymentSheet = new PaymentSheet(cartActivity, this::onPaymentResult);
    }

    public void createCustomer(double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    customerId = object.getString("id");
                    getEphemeralKey(totalAmount);
                } catch (JSONException e) {
                    Log.e("PaymentService", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentService", "Volley Error: " + error.getMessage());
                Toast.makeText(cartActivity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(cartActivity);
        requestQueue.add(stringRequest);
    }

    private void getEphemeralKey(double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ephemeralKey = object.getString("id");
                    getClientSecret(customerId, ephemeralKey, totalAmount);
                } catch (JSONException e) {
                    Log.e("PaymentService", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentService", "Volley Error: " + error.getMessage());
                Toast.makeText(cartActivity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                headers.put("Stripe-Version", "2024-06-20");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(cartActivity);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerId, String ephemeralKey, double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    clientSecret = object.getString("client_secret");
                    paymentFlow();
                } catch (JSONException e) {
                    Log.e("PaymentService", "JSON Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentService", "Volley Error: " + error.getMessage());
                Toast.makeText(cartActivity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
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

        RequestQueue requestQueue = Volley.newRequestQueue(cartActivity);
        requestQueue.add(stringRequest);
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(clientSecret);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(cartActivity, "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            User user = LoginActivity.getCurrentUser();
            cartActivity.savePurchaseToHistory();
            user.clearCart();
            DatabaseManager.clearCartInDatabase(cartActivity, user.getUserId());
            cartActivity.startActivity(new Intent(cartActivity, MainActivity.class));
            cartActivity.finish();
            Toast.makeText(cartActivity, "Payment Success", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e("PaymentService", "Payment Failed: " + failedResult.getError().getMessage());
            Toast.makeText(cartActivity, "Payment Failed: " + failedResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}