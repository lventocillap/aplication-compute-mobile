package com.example.storecomputer.Model;

public class Promotion {
    private String imageUrl;
    private String title;
    private String description;

    public Promotion(String imageUrl, String title, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}