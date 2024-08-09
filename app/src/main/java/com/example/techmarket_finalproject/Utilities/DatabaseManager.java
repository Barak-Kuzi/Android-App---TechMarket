package com.example.techmarket_finalproject.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Interfaces.UserCallBack;
import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";    // TAG for logging

    public static void initializeDatabaseWithProducts(Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ArrayList<Product> products = DataManager.getAllProducts();
                    addAllProductsToDatabase(context, products);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to check products in database.", databaseError.toException());
            }
        });
    }


    public static void addUserToDatabase(Context context, String userId, String name, String email, String password, String address, String phone, boolean isAdmin) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(userId, name, email, password, address, phone, isAdmin);
        user.setRememberMe(false);

        user.setCart(new HashMap<>());
        user.setFavoriteProducts(new ArrayList<>());

        try {
            databaseReference.child(user.getUserId()).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "User registered successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to register user.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error adding user to database", e);
            Toast.makeText(context, "An error occurred while registering the user.", Toast.LENGTH_SHORT).show();
        }
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

    public static void updateRememberLastUserFlag(String userId, boolean rememberLastUser) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("rememberMe").setValue(rememberLastUser);
    }

    public static void getRememberLastUserFlag(String userId, GenericCallBack<Boolean> callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("rememberMe");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean rememberMe = dataSnapshot.getValue(Boolean.class);
                callback.onResponse(rememberMe != null && rememberMe);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onResponse(false);
            }
        });
    }

    public static void updateUserProfileImage(String userId, String imageUrl, GenericCallBack<Boolean> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("profileImageUrl").setValue(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResponse(true);
            } else {
                callback.onResponse(false);
            }
        });
    }

    public static void uploadImageToFirebaseStorage(User user, Uri imageUri, GenericCallBack<String> callback) {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile_images/" + user.getUserId() + ".jpg");

            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        callback.onResponse(imageUrl);
                    })
            ).addOnFailureListener(e ->
                    callback.onFailure(DatabaseError.fromException(e))
            );
        } else {
            callback.onFailure(DatabaseError.fromException(new Exception("Image URI is null")));
        }
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

    public static void clearCartInDatabase(Context context, String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Cart cleared successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to clear cart.", Toast.LENGTH_SHORT).show();
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

    public static void addProductToDatabase(Context context, Product product) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(product.getProductId()).setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Product added successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void removeProductFromDatabase(Context context, String productId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(productId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Product deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateFavoriteProductsInDatabase(Context context, String userId, List<String> favoriteProducts) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favoriteProducts");
        databaseReference.setValue(favoriteProducts).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Favorites updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setBannerSliderImages(Context context, GenericCallBack<ArrayList<SliderItems>> callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("banners");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SliderItems> sliderItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SliderItems sliderItem = snapshot.getValue(SliderItems.class);
                    sliderItems.add(sliderItem);
                }
                callback.onResponse(sliderItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read banner slider images from database.", databaseError.toException());
                callback.onFailure(databaseError);
            }
        });
    }

    public static void getAllCategoriesFromDatabase(GenericCallBack<ArrayList<Category>> callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Category> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categories.add(category);
                }
                callback.onResponse(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError);
            }
        });
    }

    public static void addNewListenerForUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(LoginActivity.getCurrentUser().getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                LoginActivity.setCurrentUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // pass
            }
        });
    }
}
