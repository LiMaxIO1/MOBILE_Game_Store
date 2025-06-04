package com.example.final_game_store;

public class Game {
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private float rating;

    public Game() {
        // Обязательный пустой конструктор для Firebase
    }

    public Game(String name, String description, String imageUrl, double price, float rating) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
    }

    // --- Геттеры и сеттеры ---

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public float getRating() { return rating; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(double price) { this.price = price; }
    public void setRating(float rating) { this.rating = rating; }
}

