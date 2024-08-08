package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techmarket_finalproject.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    Button btnPayment;
    String PublishableKey = "pk_test_51OKSUWAFluX64oDUOw8ciGfX0NspFHsgHPJbitFIUvz5bMZYEfaErkyWUM95ENoZFqT2oGrHNPsE4H8UQL6Wyt5J00auc6GJZo";
    String SecretKey = "sk_test_51OKSUWAFluX64oDUMlxnBFHzPUO76MiRgTM8cA1Lo0VwuicOL6pKW4cJxGkFQi44Ngxa5PGUQBeyC9eR6L0tZrHM00lxrk3x4s";
    String CustomerId;
    String EphemeralKey;
    String ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        btnPayment = findViewById(R.id.button);

        PaymentConfiguration.init(getApplicationContext(), PublishableKey);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PaymentActivity", "Payment button clicked");
                paymentFlow();
            }
        });

        //new
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        createCustomer(totalAmount);
    }

    private void createCustomer(double totalAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomerId = object.getString("id");
                    Toast.makeText(PaymentActivity.this, "Customer ID: " + CustomerId, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PaymentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Log.d("PaymentActivity", "Starting payment flow");
        paymentSheet.presentWithPaymentIntent(ClientSecret);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("PaymentActivity", "Payment Canceled");
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d("PaymentActivity", "Payment Success");
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
                    Toast.makeText(PaymentActivity.this, "Epherical Key: " + EphemeralKey, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PaymentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PaymentActivity.this, "Client Secret: " + ClientSecret, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PaymentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);


                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("amount", "1000");
//                params.put("currency", "usd");
//                params.put("customer", customerId);
//                params.put("automatic_payment_methods[enabled]", "true");
//
//                return params;
//            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", String.valueOf((int) (totalAmount * 100))); // Convert to cents
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