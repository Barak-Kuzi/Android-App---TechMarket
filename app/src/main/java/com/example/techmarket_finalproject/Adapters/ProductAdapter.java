package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ViewholderStoreProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList) {
        this.productList = (ArrayList<Product>) productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewholderStoreProductBinding binding = ViewholderStoreProductBinding.inflate(layoutInflater, parent, false);
        this.context = parent.getContext();
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<Product> newProducts) {
        this.productList = (ArrayList<Product>) newProducts;
        notifyDataSetChanged();
    }

    public void updateData(List<Product> newProductList) {
        this.productList = (ArrayList<Product>) newProductList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderStoreProductBinding binding;

        public ProductViewHolder(ViewholderStoreProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.titleTextOrder.setText(product.getTitle());
            binding.priceForOneItemText.setText(String.valueOf("$" + product.getPrice()));

            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                ImageLoader.loadImage(binding.itemImageOrder, product.getImageUri());
            } else {
                ImageLoader.loadImage(binding.itemImageOrder, product.getImageResourceId());
            }

        }
    }
}