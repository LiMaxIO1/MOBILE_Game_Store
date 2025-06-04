package com.example.final_game_store;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<CartItem> cartItems = new ArrayList<>();

    // Добавление товара в корзину
    public static void addToCart(CartItem newItem) {
        for (CartItem item : cartItems) {
            if (item.getGameId().equals(newItem.getGameId())) {
                // Если товар уже есть — увеличить количество
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        // Если товара нет — добавить новый
        cartItems.add(newItem);
    }

    // Удаление товара по gameId
    public static void removeFromCart(String gameId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getGameId().equals(gameId)) {
                cartItems.remove(i);
                break;
            }
        }
    }

    // Получение всех товаров
    public static List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Возвращаем копию списка
    }

    // Очистить корзину
    public static void clearCart() {
        cartItems.clear();
    }

    // Получить общую сумму
    public static double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
