package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ViewholderPopularProductsBinding;

import java.util.ArrayList;

public class PopularProductsAdapter extends RecyclerView.Adapter<PopularProductsAdapter.Viewholder> {

    private final ArrayList<Product> products;
    private Context context;
    private ViewholderPopularProductsBinding viewholderPopularProductsBinding;

    public PopularProductsAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public PopularProductsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.viewholderPopularProductsBinding = ViewholderPopularProductsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.context = parent.getContext();
        return new Viewholder(viewholderPopularProductsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductsAdapter.Viewholder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderPopularProductsBinding binding;

        public Viewholder(ViewholderPopularProductsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.productTitleText.setText(product.getTitle());
            binding.oldPriceProductText.setText("$" + product.getPrice());
            binding.oldPriceProductText.setPaintFlags(binding.oldPriceProductText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.newPriceProductText.setText("$" + product.getNewPrice());
            binding.ratingBarProduct.setRating((float) product.getScore());
            binding.ratingProductText.setText("(" + product.getScore() + ")");
            binding.reviewsProductText.setText(String.valueOf(product.getReview()));

            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                ImageLoader.loadImage(binding.productImageView, product.getImageUri());
            } else {
                ImageLoader.loadImage(binding.productImageView, product.getImageResourceId());
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            });
        }
    }
}
