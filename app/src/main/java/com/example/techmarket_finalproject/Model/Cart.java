package com.example.techmarket_finalproject.Model;

import java.util.HashMap;

public class Cart {
    private final HashMap<String, Integer> cartProducts;

    public Cart() {
        this.cartProducts = new HashMap<>();
    }

    public HashMap<String, Integer> getCartProducts() {
        return cartProducts;
    }

    public void addProductToCart(String productId, int quantity) {
        if(this.cartProducts.containsKey(productId)) {
            this.cartProducts.put(productId, this.cartProducts.get(productId) + quantity);
        } else {
            this.cartProducts.put(productId, quantity);
        }
    }

    public void removeProductFromCart(String productId) {
        this.cartProducts.remove(productId);
    }

    public void clearCart() {
        this.cartProducts.clear();
    }
}
