package com.example.techmarket_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapter.PopularAdapter;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.databinding.ActivityMainBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        statusBarColor();
        initRecyclerView();
        bottomNavigation();
    }

    private void bottomNavigation() {
        activityMainBinding.cartMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
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

        activityMainBinding.popularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMainBinding.popularView.setAdapter(new PopularAdapter(items));
    }
}