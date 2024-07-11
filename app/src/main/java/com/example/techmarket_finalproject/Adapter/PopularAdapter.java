package com.example.techmarket_finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.databinding.ViewholderPopularListBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {

    private ArrayList<PopularDomain> items;
    Context context;
    ViewholderPopularListBinding listBinding;

    public PopularAdapter(ArrayList<PopularDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listBinding = ViewholderPopularListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(listBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        listBinding.titleText.setText(items.get(position).getTitle());
        listBinding.priceText.setText("$" + items.get(position).getPrice());
        listBinding.scoreText.setText("" + items.get(position).getScore());
        listBinding.reviewText.setText("" + items.get(position).getReview());

        int drawableResourced = holder.itemView.getResources().getIdentifier(items.get(position).getImageUrl(),
                "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(listBinding.itemImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(ViewholderPopularListBinding listBinding) {
            super(listBinding.getRoot());
        }
    }
}
