package com.example.techmarket_finalproject.Util;

// DatabaseManager.java
import android.content.Context;
import android.widget.Toast;

import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DatabaseManager {
    private DatabaseReference databaseReference;

    public DatabaseManager() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static void addUserToDatabase(Context context, String userId, String name, String email, String password, String address, String phone) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(userId, name, email, password, address, phone);
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
}
