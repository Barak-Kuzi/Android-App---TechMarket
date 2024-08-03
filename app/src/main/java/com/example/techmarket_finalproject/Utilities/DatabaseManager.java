package com.example.techmarket_finalproject.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Interfaces.UserCallBack;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";    // TAG for logging

    public static void addUserToDatabase(Context context, String userId, String name, String email, String password, String address, String phone) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(userId, name, email, password, address, phone);

        user.setCart(new HashMap<>());

        databaseReference.child(user.getUserId()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "User registered successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to register user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getUserFromDatabase(String userId, UserCallBack callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onFailure(DatabaseError.fromException(new Exception("User not found")));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError);
            }
        });
    }

    public static void updateUserInDatabase(Context context, String userId, Map<String, Object> userMap) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).updateChildren(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "User updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addProductToCartInDatabase(Context context, String userId, String productId, int quantity) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        databaseReference.child(productId).setValue(quantity).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Product added to cart successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add product to cart.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void removeProductFromCartInDatabase(Context context, String userId, String productId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        databaseReference.child(productId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Product removed from cart successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove product from cart.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addAllProductsToDatabase(Context context, ArrayList<Product> products) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        for (Product product : products) {
            databaseReference.child(product.getProductId()).setValue(product).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Product added successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add product.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void getAllProductsFromDatabase(GenericCallBack<ArrayList<Product>> callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Product> products = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                }
                callback.onResponse(products);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read products from database.", error.toException());
            }
        });
    }
}
