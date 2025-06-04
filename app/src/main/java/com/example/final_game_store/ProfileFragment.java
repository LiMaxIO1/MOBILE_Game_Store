package com.example.final_game_store;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.final_game_store.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            binding.emailTextView.setText(user.getEmail());
            binding.passwordTextView.setText("********"); // Просто заглушка
            loadUserData(user.getUid());
        }

        binding.logoutButton.setOnClickListener(v -> {
            auth.signOut();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AuthFragment())
                    .commit();
        });

        binding.changeNicknameButton.setOnClickListener(v -> showNicknameChangeDialog());

        return binding.getRoot();
    }

    private void loadUserData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String nickname = snapshot.getString("nickname");
                        binding.nicknameTextView.setText(nickname != null ? nickname : "User123");
                    } else {
                        binding.nicknameTextView.setText("User123");
                    }
                })
                .addOnFailureListener(e -> {
                    binding.nicknameTextView.setText("User123");
                    Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                });
    }

    private void showNicknameChangeDialog() {
        EditText input = new EditText(requireContext());
        input.setHint("Введите новый никнейм");

        new AlertDialog.Builder(requireContext())
                .setTitle("Сменить никнейм")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newNickname = input.getText().toString().trim();
                    if (!newNickname.isEmpty()) {
                        saveNewNickname(newNickname);
                    }
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveNewNickname(String newNickname) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("nickname", newNickname);

        db.collection("users").document(user.getUid())
                .set(updates, SetOptions.merge()) // безопасно: создаст если нет
                .addOnSuccessListener(unused -> {
                    binding.nicknameTextView.setText(newNickname);
                    Toast.makeText(getContext(), "Никнейм обновлён", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
