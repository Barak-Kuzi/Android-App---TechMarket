package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Activity.LoginActivity;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ViewholderProductsListOrderBinding;

import java.util.HashMap;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private int orderPosition;

    public OrderProductAdapter(Context context, List<Product> productList, int orderPosition) {
        this.context = context;
        this.productList = productList;
        this.orderPosition = orderPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderProductsListOrderBinding binding = ViewholderProductsListOrderBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            ImageLoader.loadImage(holder.binding.productImageOrderHistory, product.getImageUri());
        } else {
            ImageLoader.loadImage(holder.binding.productImageOrderHistory, product.getImageResourceId());
        }

        // Extract the purchase history for the current order and quantity for the current product
        HashMap<String, Object> purchase = LoginActivity.getCurrentUser().getPurchaseHistory().get(orderPosition);
        HashMap<String, Long> productsMap = (HashMap<String, Long>) purchase.get("products");
        long quantity = productsMap.getOrDefault(product.getProductId(), 0L);

        holder.binding.productTitleOrderHistory.setText(product.getTitle());
        holder.binding.productQuantityOrderHistory.setText("Quantity: " + quantity);
        holder.binding.productPriceOrderHistory.setText("$" + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewholderProductsListOrderBinding binding;

        public ViewHolder(@NonNull ViewholderProductsListOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}