package com.example.techmarket_finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.R;
import com.example.techmarket_finalproject.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private OnCategoryClickListener onCategoryClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCategoryBinding viewholderCategoryBinding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(viewholderCategoryBinding, onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.viewholderCategoryBinding.categoryTitle.setText(category.getTitle());
        Glide.with(context)
                .load(category.getImageUrl())
                .into(holder.viewholderCategoryBinding.categoryImage);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewholderCategoryBinding viewholderCategoryBinding;
        OnCategoryClickListener onCategoryClickListener;

        public CategoryViewHolder(ViewholderCategoryBinding viewholderCategoryBinding, OnCategoryClickListener onCategoryClickListener) {
            super(viewholderCategoryBinding.getRoot());
            this.viewholderCategoryBinding = viewholderCategoryBinding;
            this.onCategoryClickListener = onCategoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoryClickListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }
}