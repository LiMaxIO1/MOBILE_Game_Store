package com.example.final_game_store;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    public static void addToCart(CartItem item) {
        for (CartItem existing : cartItems) {
            if (existing.getGameId().equals(item.getGameId())) {
                existing.incrementQuantity();
                return;
            }
        }
        cartItems.add(item);
    }

    public static void removeFromCart(String gameId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getGameId().equals(gameId)) {
                cartItems.remove(i);
                break;
            }
        }
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void clearCart() {
        cartItems.clear();
    }

    public static double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Синхронизация корзины в Firebase (на выходе из приложения)
    public static void syncCartToFirebase() {
        String uid = getUid();
        if (uid == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Map<String, Object>> items = new ArrayList<>();

        for (CartItem item : cartItems) {
            Map<String, Object> map = new HashMap<>();
            map.put("gameId", item.getGameId());
            map.put("name", item.getName());
            map.put("price", item.getPrice());
            map.put("imageUrl", item.getImageUrl());
            map.put("quantity", item.getQuantity());
            items.add(map);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);

        db.collection("cart").document(uid)
                .set(data, SetOptions.merge());
    }

    // Загрузка корзины из Firebase (при входе)
    public static void loadCartFromFirebase(OnCartLoadedListener listener) {
        String uid = getUid();
        if (uid == null) return;

        FirebaseFirestore.getInstance().collection("cart")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    cartItems.clear();
                    if (snapshot.exists()) {
                        List<Map<String, Object>> items = (List<Map<String, Object>>) snapshot.get("items");
                        if (items != null) {
                            for (Map<String, Object> map : items) {
                                CartItem item = new CartItem(
                                        (String) map.get("gameId"),
                                        (String) map.get("name"),
                                        (String) map.get("imageUrl"),
                                        ((Number) map.get("price")).doubleValue(),
                                        ((Number) map.get("quantity")).intValue()
                                );
                                cartItems.add(item);
                            }
                        }
                    }
                    if (listener != null) listener.onCartLoaded(cartItems);
                });
    }

    public interface OnCartLoadedListener {
        void onCartLoaded(List<CartItem> cartItems);
    }

    private static String getUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }
}
