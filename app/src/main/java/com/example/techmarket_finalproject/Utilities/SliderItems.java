package com.example.techmarket_finalproject.Utilities;

public class SliderItems {
    private String url;
    private int imageResourceId;

    public SliderItems() {
    }

    public SliderItems(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public SliderItems(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
