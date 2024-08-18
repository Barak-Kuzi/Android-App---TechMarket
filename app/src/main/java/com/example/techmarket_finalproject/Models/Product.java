package com.example.techmarket_finalproject.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product implements Serializable {
    private String productId;
    private String title;
    private int imageResourceId;
    private int review;
    private double score;
    private double price;
    private String description;
    private boolean isFavorite;
    private CategoryEnum category;
    private String imageUri;
    private boolean isOnSale;
    private double newPrice;

    public Product() {
    }

    public Product(String productId, String title, int imageResourceId, int review, double score, double price, String description, CategoryEnum category, boolean isOnSale, double discountPercentage) {
        this.productId = productId;
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.review = review;
        this.score = score;
        this.price = price;
        this.description = description;
        this.isFavorite = false;
        this.category = category;
        this.isOnSale = isOnSale;
        this.newPrice = calculateDiscountedPrice(price, discountPercentage);
    }

    public Product(String productId, String title, String imageUri, int review, double score, double price, String description, CategoryEnum category, boolean isOnSale, double discountPercentage) {
        this.productId = productId;
        this.title = title;
        this.imageUri = imageUri;
        this.review = review;
        this.score = score;
        this.price = price;
        this.description = description;
        this.isFavorite = false;
        this.category = category;
        this.isOnSale = isOnSale;
        this.newPrice = calculateDiscountedPrice(price, discountPercentage);
    }

    private double calculateDiscountedPrice(double price, double discountPercentage) {
        double discountedPrice = price * (1 - (discountPercentage / 100));
        BigDecimal bd = new BigDecimal(Double.toString(discountedPrice));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
