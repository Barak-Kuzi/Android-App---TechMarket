package com.example.techmarket_finalproject.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techmarket_finalproject.Activity.DetailActivity;
import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;
import com.example.techmarket_finalproject.Models.User;
import com.example.techmarket_finalproject.Utilities.ImageLoader;
import com.example.techmarket_finalproject.databinding.ViewholderCartBinding;
import com.example.techmarket_finalproject.Models.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private final ArrayList<Product> products;
    private final User user;
    private final HashMap<String, Integer> userCart;
    private Context context;
    private ViewholderCartBinding viewholderCartBinding;
    private UpdateQuantityProductsListener updateQuantityProductsListener;

    public CartAdapter(ArrayList<Product> products, User user, HashMap<String, Integer> userCart, UpdateQuantityProductsListener updateQuantityProductsListener) {
        this.products = products;
        this.user = user;
        this.userCart = userCart;
        this.updateQuantityProductsListener = updateQuantityProductsListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewholderCartBinding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(viewholderCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        Product product = products.get(position);
        int productQuantity = userCart.get(product.getProductId());
        double productPrice = product.getPrice();
        double totalPrice = productPrice * productQuantity;
        String formattedTotalPrice = String.format("%.2f", totalPrice);

        holder.binding.productTitleCart.setText(product.getTitle());
        holder.binding.priceForOneProductCart.setText("$" + product.getPrice());
        holder.binding.totalProductPriceCart.setText("$" + formattedTotalPrice);
        holder.binding.quantityProductText.setText(String.valueOf(productQuantity));

        if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
            ImageLoader.loadImage(holder.binding.productImageCart, product.getImageUri());
        } else {
            ImageLoader.loadImage(holder.binding.productImageCart, product.getImageResourceId());
        }

        holder.binding.plusButton.setOnClickListener(v -> {
            user.increaseQuantity(product.getProductId(), v.getContext(), user.getUserId(), () -> {
                notifyItemChanged(position);
                updateQuantityProductsListener.update();
            });
        });


        holder.binding.minusButton.setOnClickListener(v -> {
            user.decreaseQuantity(product.getProductId(), v.getContext(), user.getUserId(), () -> {
                if (user.productInCart(product.getProductId())) {
                    notifyItemChanged(position);
                } else {
                    removeProduct(position);
                }
                updateQuantityProductsListener.update();
            });
        });

        holder.binding.removeProductCart.setOnClickListener(v -> {
            user.removeProductFromCart(product.getProductId(), v.getContext(), user.getUserId(), () -> {
                removeProduct(position);
                updateQuantityProductsListener.update();
            });
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

    }

    private void removeProduct(int position) {
        products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, products.size());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getProductId().hashCode(); // Use product ID as stable ID
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
