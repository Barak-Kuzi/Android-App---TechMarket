package com.example.techmarket_finalproject.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String title;
    private int imageResourceId;
    private int review;
    private double score;
    private double price;
    private String description;
    private boolean isFavorite;
    private Category category;

    public Product() {
    }

    public Product(String productId, String title, int imageResourceId, int review, double score, double price, String description, Category category) {
        this.productId = productId;
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.review = review;
        this.score = score;
        this.price = price;
        this.description = description;
        this.isFavorite = false;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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
