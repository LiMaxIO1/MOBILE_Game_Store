package com.example.final_game_store;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_game_store.R;
import com.example.final_game_store.CartAdapter;
import com.example.final_game_store.CartManager;

public class CartDialogFragment extends DialogFragment {

    private CartAdapter adapter;
    private TextView totalTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cart, null);
        dialog.setContentView(view);

        // Инициализация UI
        RecyclerView recyclerView = view.findViewById(R.id.cartRecyclerView);
        totalTextView = view.findViewById(R.id.totalTextView);
        Button closeButton = view.findViewById(R.id.closeButton);
        Button clearButton = view.findViewById(R.id.clearButton);

        // Установка адаптера
        adapter = new CartAdapter(getContext(), CartManager.getCartItems(), () -> {
            updateTotal();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Обработчики кнопок
        closeButton.setOnClickListener(v -> dismiss());

        clearButton.setOnClickListener(v -> {
            CartManager.clearCart();
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        updateTotal();
        return dialog;
    }

    private void updateTotal() {
        double total = CartManager.getTotalPrice();
        totalTextView.setText(String.format("Итого: %.2f ₽", total));
    }
}


