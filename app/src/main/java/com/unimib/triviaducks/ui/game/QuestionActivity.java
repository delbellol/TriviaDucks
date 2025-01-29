package com.unimib.triviaducks.ui.game;

import static com.unimib.triviaducks.util.Constants.CATEGORY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.util.MusicService;

public class QuestionActivity extends AppCompatActivity {

    public QuestionActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isMusicEnabled = preferences.getBoolean("MusicEnabled", true);
        boolean isNightMode = preferences.getBoolean("ThemeNightMode", false);

        if (isMusicEnabled) {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction("ON"); // Usa l'azione ON per avviare la musica
            startService(intent);
        }
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}