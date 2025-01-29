package com.unimib.triviaducks.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.MusicService;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
//        boolean isMusicEnabled = preferences.getBoolean("MusicEnabled", true);
//        boolean isNightMode = preferences.getBoolean("ThemeNightMode", false);
//
//        if (isMusicEnabled) {
//            Intent intent = new Intent(this, MusicService.class);
//            intent.setAction("ON"); // Usa l'azione ON per avviare la musica
//            startService(intent);
//        }
//        if (isNightMode) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

        setContentView(R.layout.activity_main); //il layout viene settato dopo aver impostato il tema
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Ferma la musica quando l'app va in background
        stopMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Ferma la musica quando l'app va in background
        stopMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ripristina la musica quando l'app ritorna in primo piano
        resumeMusic();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Assicurati che la musica parta all'avvio dell'attività
        resumeMusic();
    }

    private void stopMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("OFF");
        startService(intent); // Ferma la musica
    }

    private void resumeMusic() {
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isMusicEnabled = preferences.getBoolean("MusicEnabled", true);

        if (isMusicEnabled) {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction("ON"); // Riavvia la musica
            startService(intent);
        }
    }
}
