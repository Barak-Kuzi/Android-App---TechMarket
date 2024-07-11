package com.example.techmarket_finalproject.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techmarket_finalproject.Adapter.CartAdapter;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Util.ChangeNumberItemsListener;
import com.example.techmarket_finalproject.Util.ManagmentCart;
import com.example.techmarket_finalproject.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding activityCartBinding;
    private ManagmentCart managmentCart;
    double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());
        managmentCart = new ManagmentCart(this);

        statusBarColor();
        setVariable();
        initList();
        calculateTotalPrice();
    }

    private void statusBarColor() {
        Window window = CartActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.purple_Dark));
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()){
            activityCartBinding.emptyCartText.setVisibility(View.VISIBLE);
            activityCartBinding.scrollViewItems.setVisibility(View.GONE);
        } else {
            activityCartBinding.emptyCartText.setVisibility(View.GONE);
            activityCartBinding.scrollViewItems.setVisibility(View.VISIBLE);
        }

        activityCartBinding.cartView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityCartBinding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateTotalPrice();
            }
        }));
    }

    private void calculateTotalPrice() {
        double percentTax = 0.02;
        double deliveryPrice = 10;

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + deliveryPrice) * 100) / 100;
        double itemTotal = Math.round((managmentCart.getTotalFee() + tax) * 100) / 100;

        activityCartBinding.totalFeeText.setText("$" + itemTotal);
        activityCartBinding.taxText.setText("$" + tax);
        activityCartBinding.deliveryText.setText("$" + deliveryPrice);
        activityCartBinding.totalOrderText.setText("$" + total);
    }

    private void setVariable() {
        activityCartBinding.backButtonViewCart.setOnClickListener(v -> finish());

    }
}