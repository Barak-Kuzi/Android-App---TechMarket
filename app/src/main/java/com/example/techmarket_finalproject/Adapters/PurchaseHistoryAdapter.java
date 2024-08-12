package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Utilities.ProductManager;
import com.example.techmarket_finalproject.databinding.ViewholderOrderListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {

    private Context context;
    private List<HashMap<String, Object>> purchaseHistory;

    public PurchaseHistoryAdapter(Context context, List<HashMap<String, Object>> purchaseHistory) {
        this.context = context;
        this.purchaseHistory = purchaseHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderOrderListBinding binding = ViewholderOrderListBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> purchase = purchaseHistory.get(position);

        String orderNumber = "<b>Order Number:</b> " + (position + 1);
        String purchaseDate = "<b>Purchase Date:</b> " + ((String) purchase.get("purchaseDate"));
        String totalPrice = "<b>Total Price:</b> $" + String.format("%.2f", purchaseHistory.get(position).get("totalPrice"));

        holder.binding.orderNumber.setText(HtmlCompat.fromHtml(orderNumber, HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.binding.totalPrice.setText(HtmlCompat.fromHtml(totalPrice, HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.binding.purchaseDate.setText(HtmlCompat.fromHtml(purchaseDate, HtmlCompat.FROM_HTML_MODE_LEGACY));

        HashMap<String, Integer> productsMap = (HashMap<String, Integer>) purchase.get("products");
        List<Product> productList = new ArrayList<>();
        for (String productId : productsMap.keySet()) {
            Product product = ProductManager.getProductById(productId);
            productList.add(product);
        }

        OrderProductAdapter orderProductAdapter = new OrderProductAdapter(context, productList, position);
        holder.binding.productsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.productsRecyclerView.setAdapter(orderProductAdapter);
    }

    @Override
    public int getItemCount() {
        return purchaseHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewholderOrderListBinding binding;

        public ViewHolder(@NonNull ViewholderOrderListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}