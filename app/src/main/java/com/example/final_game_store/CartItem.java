package com.example.final_game_store;

public class CartItem {
    private String gameId;
    private String name;
    private String imageUrl;
    private double price;
    private int quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(String gameId, String name, String imageUrl, double price, int quantity) {
        this.gameId = gameId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }
}
