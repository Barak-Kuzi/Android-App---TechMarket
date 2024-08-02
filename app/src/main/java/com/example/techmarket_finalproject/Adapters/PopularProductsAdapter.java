package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.databinding.ViewholderPopularProductBinding;

import java.util.ArrayList;

public class PopularProductsAdapter extends RecyclerView.Adapter<PopularProductsAdapter.Viewholder> {

    private final ArrayList<Product> products;
    private User user;
    private Context context;
    private ViewholderPopularProductBinding productBinding;

    public PopularProductsAdapter(ArrayList<Product> products, User user) {
        this.products = products;
        this.user = user;
    }

    @NonNull
    @Override
    public PopularProductsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.productBinding = ViewholderPopularProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.context = parent.getContext();
        return new Viewholder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductsAdapter.Viewholder holder, int position) {
        Product product = products.get(position);

        productBinding.titleText.setText(product.getTitle());
        productBinding.priceText.setText("$" + product.getPrice());
        productBinding.scoreText.setText(String.valueOf(product.getScore()));
        productBinding.reviewText.setText(String.valueOf(product.getReview()));

//        int drawableResourced = holder.itemView.getResources().getIdentifier(product.getImageResourceId(),
//                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(product.getImageResourceId())
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(productBinding.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product", products.get(position));
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getProduct(int position) {
        return this.products.get(position);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(ViewholderPopularProductBinding productBinding) {
            super(productBinding.getRoot());
        }
    }
}
