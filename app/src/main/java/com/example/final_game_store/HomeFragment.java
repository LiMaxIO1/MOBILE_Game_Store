package com.example.final_game_store;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_game_store.R;
import com.example.final_game_store.GameAdapter;
import com.example.final_game_store.Game;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView gamesRecyclerView, carouselRecyclerView;
    private GameAdapter gameAdapter, carouselAdapter;
    private List<Game> gameList = new ArrayList<>();
    private EditText searchEditText;
    private ImageButton cartButton;

    private int carouselIndex = 0;
    private final Handler handler = new Handler();
    private final Runnable carouselRunnable = new Runnable() {
        @Override
        public void run() {
            if (carouselAdapter == null || carouselAdapter.getItemCount() == 0) return;

            if (carouselIndex >= carouselAdapter.getItemCount()) {
                carouselIndex = 0;
            }

            carouselRecyclerView.smoothScrollToPosition(carouselIndex);
            carouselIndex++;

            handler.postDelayed(this, 5000); // каждые 5 сек
        }
    };

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference gamesRef = db.collection("games");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gamesRecyclerView = view.findViewById(R.id.gamesRecyclerView);
        carouselRecyclerView = view.findViewById(R.id.carouselRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        cartButton = view.findViewById(R.id.cartButton);

        setupRecyclerViews();
        loadGamesFromFirebase();
        setupSearch();

        cartButton.setOnClickListener(v -> {
            new CartDialogFragment().show(getParentFragmentManager(), "cartDialog");
        });

        return view;
    }

    private void setupRecyclerViews() {
        // Список игр
        gameAdapter = new GameAdapter(getContext(), gameList, this::openGameDetails);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesRecyclerView.setAdapter(gameAdapter);

        // Карусель
        carouselAdapter = new GameAdapter(getContext(), gameList, this::openGameDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(layoutManager);
        carouselRecyclerView.setAdapter(carouselAdapter);
    }

    private void loadGamesFromFirebase() {
        gamesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                gameList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Game game = doc.toObject(Game.class);
                    gameList.add(game);
                }
                gameAdapter.updateList(new ArrayList<>(gameList));
                carouselAdapter.updateList(new ArrayList<>(gameList));
                carouselIndex = 0; // сброс для карусели
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterGames(s.toString());
            }
        });
    }

    private void filterGames(String query) {
        List<Game> filteredList = new ArrayList<>();
        for (Game game : gameList) {
            if (game.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(game);
            }
        }
        gameAdapter.updateList(filteredList);
    }

    private void openGameDetails(Game game) {
        Bundle bundle = new Bundle();
        bundle.putString("name", game.getName());
        bundle.putString("description", game.getDescription());
        bundle.putString("imageUrl", game.getImageUrl());
        bundle.putDouble("price", game.getPrice());
        bundle.putFloat("rating", game.getRating());

        GameDetailsFragment fragment = new GameDetailsFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(carouselRunnable, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(carouselRunnable);
    }
}




