package com.example.final_game_store;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            return loadFragment(selectedFragment);
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            loadFragment(new AuthFragment());
            bottomNav.setVisibility(View.GONE);
        } else {
            loadFragment(new HomeFragment());
            bottomNav.setVisibility(View.VISIBLE);

            // Загрузить корзину из Firebase
            CartManager.loadCartFromFirebase(cartItems -> {
                // Можно обновить UI корзины, если нужно
            });
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Синхронизировать корзину с Firebase при закрытии
        CartManager.syncCartToFirebase();
    }
}
