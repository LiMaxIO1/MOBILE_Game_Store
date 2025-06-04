package com.example.final_game_store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameDetailsFragment extends Fragment {

    private ImageView gameImageView;
    private TextView gameNameTextView, gamePriceTextView, gameRatingTextView, gameDescriptionTextView;
    private Button addToCartButton;

    private String name, description, imageUrl;
    private double price;
    private float rating;

    public GameDetailsFragment() {
        // Required empty public constructor
    }

    public static GameDetailsFragment newInstance(Game game) {
        GameDetailsFragment fragment = new GameDetailsFragment();
        Bundle args = new Bundle();
        args.putString("name", game.getName());
        args.putString("description", game.getDescription());
        args.putString("imageUrl", game.getImageUrl());
        args.putDouble("price", game.getPrice());
        args.putFloat("rating", game.getRating());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gameImageView = view.findViewById(R.id.gameImageView);
        gameNameTextView = view.findViewById(R.id.gameNameTextView);
        gamePriceTextView = view.findViewById(R.id.gamePriceTextView);
        gameRatingTextView = view.findViewById(R.id.gameRatingTextView);
        gameDescriptionTextView = view.findViewById(R.id.gameDescriptionTextView);
        addToCartButton = view.findViewById(R.id.addToCartButton);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            description = getArguments().getString("description");
            imageUrl = getArguments().getString("imageUrl");
            price = getArguments().getDouble("price");
            rating = getArguments().getFloat("rating");

            gameNameTextView.setText(name);
            gamePriceTextView.setText(String.format("%.2f ₽", price));
            gameRatingTextView.setText("Рейтинг: " + rating);
            gameDescriptionTextView.setText(description);
            ImageLoader.loadImage(requireContext(), imageUrl, gameImageView);
        }

        addToCartButton.setOnClickListener(v -> {
            CartItem item = new CartItem();
            item.setGameId(name); // Лучше использовать уникальный ID, если есть
            item.setName(name);
            item.setPrice(price);
            item.setImageUrl(imageUrl);
            item.setQuantity(1);

            CartManager.addToCart(item);
            Toast.makeText(getContext(), "Игра добавлена в корзину", Toast.LENGTH_SHORT).show();
        });
    }
}
