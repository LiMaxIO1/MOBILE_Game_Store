package com.example.final_game_store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    private Context context;
    private List<Game> gameList;
    private OnGameClickListener listener;

    public GameAdapter(Context context, List<Game> gameList, OnGameClickListener listener) {
        this.context = context;
        this.gameList = gameList;
        this.listener = listener;
    }

    public void updateList(List<Game> newList) {
        gameList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.nameTextView.setText(game.getName());
        holder.priceTextView.setText(String.format("%.2f ₽", game.getPrice()));
        ImageLoader.loadImage(context, game.getImageUrl(), holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGameClick(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList != null ? gameList.size() : 0;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gameImageView);
            nameTextView = itemView.findViewById(R.id.gameNameTextView);
            priceTextView = itemView.findViewById(R.id.gamePriceTextView);
        }
    }
}
