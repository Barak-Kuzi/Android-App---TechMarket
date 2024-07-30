package com.example.techmarket_finalproject.Activity;

import static com.example.techmarket_finalproject.Util.DatabaseManager.getUserFromDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapter.PopularAdapter;
import com.example.techmarket_finalproject.Model.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Util.UserCallBack;
import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private TextView welcomeUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        welcomeUser = findViewById(R.id.welcome_user_name);

        statusBarColor();
        initRecyclerView();
        bottomNavigation();

        userId = getIntent().getStringExtra("userId");
        getUserFromDatabase(userId, new UserCallBack() {
            @Override
            public void onSuccess(User user) {
                welcomeUser.setText(user.getName());
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });

    }

    private void bottomNavigation() {
        activityMainBinding.cartMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        activityMainBinding.profileMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("T-shirt black", "item_1", 15, 4, 500, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Smart Watch", "item_2", 10, 4.5, 450, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Phone", "item_3", 3, 4.9, 800, "testttttttttttttttttttttttt"));
        items.add(new PopularDomain("Phone", "b_1", 3, 4.9, 800, "testttttttttttttttttttttttt"));

        activityMainBinding.popularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.popularView.setAdapter(new PopularAdapter(items));
    }
}