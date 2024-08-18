package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ViewholderFavoriteProductsBinding;
import com.example.techmarket_finalproject.databinding.ViewholderProductsListBinding;

import java.util.ArrayList;

public class FavoriteProductsAdapter extends RecyclerView.Adapter<FavoriteProductsAdapter.ViewHolder> {

    private final ArrayList<Product> favoriteProducts;
    private final User user;
    private Context context;
    private ViewholderFavoriteProductsBinding viewholderFavoriteProductsBinding;

    public FavoriteProductsAdapter(ArrayList<Product> favoriteProducts, User user) {
        this.favoriteProducts = favoriteProducts;
        this.user = user;
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        viewholderFavoriteProductsBinding = ViewholderFavoriteProductsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        this.context = parent.getContext();
//        return new ViewHolder(viewholderFavoriteProductsBinding);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewholderProductsListBinding binding = ViewholderProductsListBinding.inflate(layoutInflater, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteProductsAdapter.ViewHolder holder, int position) {
        Product product = favoriteProducts.get(position);

        if (product.isOnSale()) {
            holder.binding.currentPriceDetailText.setText("$" + product.getNewPrice());
            holder.binding.oldPriceDetailText.setVisibility(View.VISIBLE);
            holder.binding.oldPriceDetailText.setText("$" + product.getPrice());
            holder.binding.oldPriceDetailText.setPaintFlags(holder.binding.oldPriceDetailText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.binding.currentPriceDetailText.setText("$" + product.getPrice());
            holder.binding.oldPriceDetailText.setVisibility(View.GONE);
        }

        holder.binding.titleTextOrder.setText(product.getTitle());

        holder.binding.heartButton.setImageResource(R.drawable.heart);

        if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            ImageLoader.loadImage(holder.binding.itemImageOrder, product.getImageUri());
        } else {
            ImageLoader.loadImage(holder.binding.itemImageOrder, product.getImageResourceId());
        }

        holder.binding.heartButton.setOnClickListener(v -> {
            user.removeFavoriteProduct(product.getProductId());
            DatabaseManager.updateFavoriteProductsInDatabase(context, user.getUserId(), user.getFavoriteProducts());
            favoriteProducts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteProducts.size());
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        ViewholderFavoriteProductsBinding binding;
//
//        public ViewHolder(ViewholderFavoriteProductsBinding itemView) {
//            super(itemView.getRoot());
//            this.binding = itemView;
//        }
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewholderProductsListBinding binding;

        public ViewHolder(ViewholderProductsListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            itemView.heartButton.setVisibility(View.VISIBLE);
        }
    }
}
