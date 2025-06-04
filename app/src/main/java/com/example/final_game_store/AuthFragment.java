package com.example.final_game_store;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.final_game_store.R;
import com.google.firebase.auth.FirebaseAuth;

public class AuthFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView linkRegister;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        linkRegister = view.findViewById(R.id.linkRegister);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> loginUser());
        linkRegister.setOnClickListener(v -> {
            // Переход к RegisterFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(getContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                    // Переход к HomeFragment
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка входа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}





