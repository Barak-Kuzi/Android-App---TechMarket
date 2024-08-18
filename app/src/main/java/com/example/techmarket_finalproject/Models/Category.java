package com.example.techmarket_finalproject.Models;

public class Category {
    private String title;
    private int id;
    private String imageUrl;
    private int imageResourceId;

    public Category(String title, int id, int imageResourceId) {
        this.title = title;
        this.id = id;
        this.imageResourceId = imageResourceId;
    }

    public Category(String title, int id, String imageUrl) {
        this.title = title;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public Category() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
