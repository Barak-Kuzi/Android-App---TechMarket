package com.example.techmarket_finalproject.Util;

// DatabaseManager.java
import android.content.Context;
import android.widget.Toast;

import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Model.User;
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

    // Method to add data to the database
    public void addData(String path, Object data, DatabaseOperationListener listener) {
        databaseReference.child(path).setValue(data)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    // Method to update data in the database
    public void updateData(String path, Object data, DatabaseOperationListener listener) {
        databaseReference.child(path).updateChildren((Map<String, Object>) data)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    // Method to delete data from the database
    public void deleteData(String path, DatabaseOperationListener listener) {
        databaseReference.child(path).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    // Method to retrieve data from the database
    public void getData(String path, DataRetrievalListener listener) {
        databaseReference.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataRetrieved(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.toException());
            }
        });
    }

    // Listener interface for database operations
    public interface DatabaseOperationListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    // Listener interface for data retrieval
    public interface DataRetrievalListener {
        void onDataRetrieved(DataSnapshot dataSnapshot);
        void onFailure(Exception e);
    }

    public static void addUserToDatabase(String userId, String name, String email, String password, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(userId, name, email, password);
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

}
