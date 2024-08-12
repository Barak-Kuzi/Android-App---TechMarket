package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Adapters.PurchaseHistoryAdapter;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.databinding.ActivityPurchaseHistoryBinding;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private ActivityPurchaseHistoryBinding activityPurchaseHistoryBinding;
    private User user;
    private RecyclerView recyclerView;
    private PurchaseHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LoginActivity.getCurrentUser();

        if (user != null) {
            activityPurchaseHistoryBinding = ActivityPurchaseHistoryBinding.inflate(getLayoutInflater());
            setContentView(activityPurchaseHistoryBinding.getRoot());

            activityPurchaseHistoryBinding.backButtonPurchaseHistory.setOnClickListener(v -> finish());

            recyclerView = findViewById(R.id.purchase_history_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new PurchaseHistoryAdapter(this, user.getPurchaseHistory());
            recyclerView.setAdapter(adapter);
        }
    }
}