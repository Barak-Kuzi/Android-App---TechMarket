package com.example.techmarket_finalproject.Utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Activity.MainActivity;
import com.example.techmarket_finalproject.Activity.SplashActivity;
import com.example.techmarket_finalproject.Interfaces.GenericCallBack;
import com.example.techmarket_finalproject.Interfaces.UserCallBack;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class UserUtils {

    public static void fetchUserAndInitializeProductManager(Context context, String userId) {
        DatabaseManager.getUserFromDatabase(userId, new UserCallBack() {
            @Override
            public void onSuccess(User user) {
                ProductManager.initialize(context, new GenericCallBack<ArrayList<Product>>() {
                    @Override
                    public void onResponse(ArrayList<Product> response) {
                        LoginActivity.setCurrentUser(user);
                        DatabaseManager.addNewListenerForUser();
                        context.startActivity(new Intent(context, MainActivity.class));
                        if (context instanceof SplashActivity) {
                            ((SplashActivity) context).finish();
                        } else if (context instanceof LoginActivity) {
                            ((LoginActivity) context).finish();
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Toast.makeText(context, "Failed to load products.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(DatabaseError error) {
                Toast.makeText(context, "Failed to load user.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}