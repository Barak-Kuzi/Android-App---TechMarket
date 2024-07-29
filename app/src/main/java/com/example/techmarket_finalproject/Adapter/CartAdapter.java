package com.example.techmarket_finalproject.Adapter;

import android.annotation.SuppressLint;
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
import com.example.techmarket_finalproject.Util.ChangeNumberItemsListener;
import com.example.techmarket_finalproject.Util.ManagmentCart;
import com.example.techmarket_finalproject.databinding.ViewholderCartBinding;
import com.example.techmarket_finalproject.databinding.ViewholderPopularListBinding;
import com.example.techmarket_finalproject.domain.PopularDomain;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private ArrayList<PopularDomain> items;
    Context context;
    ViewholderCartBinding viewholderCartBinding;
    ChangeNumberItemsListener changeNumberItemsListener;
    ManagmentCart managmentCart;

    public CartAdapter(ArrayList<PopularDomain> items, ChangeNumberItemsListener changeNumberItemsListener) {
        this.items = items;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewholderCartBinding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        this.managmentCart = new ManagmentCart(context);
        return new Viewholder(viewholderCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        viewholderCartBinding.titleTextOrder.setText(items.get(position).getTitle());
        viewholderCartBinding.priceForOneItemText.setText("$" + items.get(position).getPrice());
        viewholderCartBinding.totalPriceText.setText("$" + Math.round(items.get(position).getPrice() * items.get(position).getNumberInCart()));
        viewholderCartBinding.amountItemsText.setText(String.valueOf(items.get(position).getNumberInCart()));

        viewholderCartBinding.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.plusNumberItem(items, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        viewholderCartBinding.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.minusNumberItem(items, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        int drawableResourced = holder.itemView.getResources().getIdentifier(items.get(position).getImageUrl(),
                "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(viewholderCartBinding.itemImageOrder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(ViewholderCartBinding listBinding) {
            super(listBinding.getRoot());
        }
    }
}
