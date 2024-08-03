package com.example.techmarket_finalproject.Models;

import static com.example.techmarket_finalproject.Utilities.DatabaseManager.addProductToCartInDatabase;
import static com.example.techmarket_finalproject.Utilities.DatabaseManager.removeProductFromCartInDatabase;

import android.content.Context;
import android.util.Log;

import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private HashMap<String, Integer> cart;
    private boolean isAdmin;
    private List<String> favoriteProducts;

    public User() {
        this.cart = new HashMap<>();
        this.favoriteProducts = new ArrayList<>();
    }

    public User(String userId, String name, String email, String password, String address, String phone, boolean isAdmin) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.cart = new HashMap<>();
        this.isAdmin = isAdmin;
        this.favoriteProducts = new ArrayList<>();
    }

    // Getter and setter for favoriteProducts
    public List<String> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(List<String> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public void addFavoriteProduct(String productId) {
        this.favoriteProducts.add(productId);
    }

    public void removeFavoriteProduct(String productId) {
        this.favoriteProducts.remove(productId);
    }

    public boolean isFavoriteProduct(String productId) {
        return this.favoriteProducts.contains(productId);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HashMap<String, Integer> getCart() {
        return cart;
    }

    public void setItemToCart(String productId, int quantity) {
        this.cart.put(productId, quantity);
    }

    public void setCart(HashMap<String, Integer> cart) {
        this.cart = cart;
    }

    public boolean productInCart(String productId) {
        return this.cart.containsKey(productId);
    }

    public int getQuantity(String productId) {
        return this.cart.get(productId);
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void removeProductFromCart(String productId) {
        this.cart.remove(productId);
    }

    public boolean cartIsEmpty() {
        return this.cart.isEmpty();
    }

    public double getTotalPrice(ArrayList<Product> cart) {
        double totalPrice = 0;
        for (Product product : cart) {
            if (this.cart.containsKey(product.getProductId())) {
                totalPrice += product.getPrice() * this.cart.get(product.getProductId());
            }
        }
        return totalPrice;
    }

    public void increaseQuantity(String productId, Context context, String userId, UpdateQuantityProductsListener updateQuantityProductsListener) {
        if (this.cart.containsKey(productId)) {
            this.cart.put(productId, this.cart.get(productId) + 1);
        }
        addProductToCartInDatabase(context, userId, productId, this.cart.get(productId));
        updateQuantityProductsListener.update();
    }

    public void decreaseQuantity(String productId, Context context, String userId, UpdateQuantityProductsListener updateQuantityProductsListener) {
        if (this.cart.containsKey(productId)) {
            int currentQuantity = this.cart.get(productId);
            if (currentQuantity > 1) {
                this.cart.put(productId, currentQuantity - 1);
                addProductToCartInDatabase(context, userId, productId, this.cart.get(productId));
            } else {
                this.cart.remove(productId);
                removeProductFromCartInDatabase(context, userId, productId);
            }
            updateQuantityProductsListener.update();
        }
    }

}
