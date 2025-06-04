package com.example.final_game_store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    private Context context;
    private List<CartItem> itemList;
    private OnItemRemovedListener removeListener;

    public CartAdapter(Context context, List<CartItem> itemList, OnItemRemovedListener removeListener) {
        this.context = context;
        this.itemList = itemList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = itemList.get(position);

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("x%d • %.2f ₽", item.getQuantity(), item.getTotalPrice()));

        ImageLoader.loadImage(context, item.getImageUrl(), holder.imageView);

        holder.removeButton.setOnClickListener(v -> {
            CartManager.removeFromCart(item.getGameId());
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());

            if (removeListener != null) {
                removeListener.onItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, priceTextView;
        ImageButton removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
