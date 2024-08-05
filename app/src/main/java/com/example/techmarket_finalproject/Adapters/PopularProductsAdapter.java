package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.example.techmarket_finalproject.databinding.ViewholderPopularProductsBinding;

import java.util.ArrayList;

public class PopularProductsAdapter extends RecyclerView.Adapter<PopularProductsAdapter.Viewholder> {

    private final ArrayList<Product> products;
    private User user;
    private Context context;
    private ViewholderPopularProductsBinding viewholderPopularProductsBinding;

    public PopularProductsAdapter(ArrayList<Product> products, User user) {
        this.products = products;
        this.user = user;
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

        viewholderPopularProductsBinding.productTitleText.setText(product.getTitle());
        viewholderPopularProductsBinding.oldPriceProductText.setText("$" + product.getPrice());
        viewholderPopularProductsBinding.oldPriceProductText
                .setPaintFlags(viewholderPopularProductsBinding.oldPriceProductText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewholderPopularProductsBinding.newPriceProductText.setText("$" + product.getPrice());
        viewholderPopularProductsBinding.ratingBarProduct.setRating((float) product.getScore());
        viewholderPopularProductsBinding.ratingProductText.setText("(" + product.getScore() + ")");
        viewholderPopularProductsBinding.reviewsProductText.setText(String.valueOf(product.getReview()));

        Glide.with(context)
                .load(product.getImageResourceId())
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(viewholderPopularProductsBinding.productImageView);

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

        public Viewholder(ViewholderPopularProductsBinding viewholderPopularProductsBinding) {
            super(viewholderPopularProductsBinding.getRoot());
        }
    }
}
