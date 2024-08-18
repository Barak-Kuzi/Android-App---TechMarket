package com.example.techmarket_finalproject.Models;

import android.content.Context;

import com.example.techmarket_finalproject.Interfaces.UpdateQuantityProductsListener;
import com.example.techmarket_finalproject.Utilities.DatabaseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String profileImageUrl;
    private boolean isAdmin;
    private boolean rememberMe;
    private HashMap<String, Integer> cart;
    private List<String> favoriteProducts;
    private List<HashMap<String, Object>> purchaseHistory;

    public User() {
        this.cart = new HashMap<>();
        this.favoriteProducts = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    public User(String userId, String name, String email, String password, String address, String phone, boolean isAdmin) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.cart = new HashMap<>();
        this.favoriteProducts = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    public List<HashMap<String, Object>> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<HashMap<String, Object>> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public void addPurchaseToHistory(HashMap<String, Object> purchase) {
        this.purchaseHistory.add(purchase);
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

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
                double productPrice = product.isOnSale() ? product.getNewPrice() : product.getPrice();
                totalPrice += productPrice * this.cart.get(product.getProductId());
            }
        }
        return totalPrice;
    }

    public void increaseQuantity(String productId, Context context, String userId, UpdateQuantityProductsListener updateQuantityProductsListener) {
        if (this.cart.containsKey(productId)) {
            this.cart.put(productId, this.cart.get(productId) + 1);
        }
        DatabaseManager.addProductToCartInDatabase(context, userId, productId, this.cart.get(productId));
        updateQuantityProductsListener.update();
    }

    public void decreaseQuantity(String productId, Context context, String userId, UpdateQuantityProductsListener updateQuantityProductsListener) {
        if (this.cart.containsKey(productId)) {
            int currentQuantity = this.cart.get(productId);
            if (currentQuantity > 1) {
                this.cart.put(productId, currentQuantity - 1);
                DatabaseManager.addProductToCartInDatabase(context, userId, productId, this.cart.get(productId));
                updateQuantityProductsListener.update();
            } else {
                removeProductFromCart(productId, context, userId, updateQuantityProductsListener);
            }
        }
    }

    public void removeProductFromCart(String productId, Context context, String userId, UpdateQuantityProductsListener updateQuantityProductsListener) {
        if (this.cart.containsKey(productId)) {
            this.cart.remove(productId);
            DatabaseManager.removeProductFromCartInDatabase(context, userId, productId);
            updateQuantityProductsListener.update();
        }
    }

}
