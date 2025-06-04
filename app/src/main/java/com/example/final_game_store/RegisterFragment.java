package com.example.final_game_store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.final_game_store.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.linkLogin.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return binding.getRoot();
    }

    private void registerUser() {
        String nickname = binding.editNickname.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();

        if (nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Пароль должен быть минимум 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = auth.getCurrentUser().getUid();

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("nickname", nickname);
                    userMap.put("email", email);

                    db.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show();
                                navigateToMainApp();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Ошибка сохранения профиля", Toast.LENGTH_SHORT).show()
                            );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void navigateToMainApp() {
        Toast.makeText(getContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment()) // ✅ заменили ID и фрагмент
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
