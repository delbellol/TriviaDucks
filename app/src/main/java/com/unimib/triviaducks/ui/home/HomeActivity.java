package com.unimib.triviaducks.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.fragment.SettingsFragment;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.MusicService;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;

    private boolean isNightMode;
    private boolean isMusicOFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext());

            isMusicOFF = sharedPreferencesUtils.readBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

            isNightMode = sharedPreferencesUtils.readBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_NIGHT_MODE);


            Intent intent = new Intent(this, MusicService.class);

            if (isMusicOFF)
                intent.setAction("OFF"); // Usa l'azione ON per avviare la musica
            else
                intent.setAction("ON");
            startService(intent);
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            setContentView(R.layout.activity_main); //il layout viene settato dopo aver impostato il tema
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }catch (Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,"Errore: "+ex.getMessage());
            else ex.printStackTrace();
        }
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
        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

        Intent intent = new Intent(this, MusicService.class);

        if (isMusicOFF)
            intent.setAction("OFF");
        else
            intent.setAction("ON");

        startService(intent);
    }
}
