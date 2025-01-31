package com.unimib.triviaducks.ui.game;


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
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.MusicService;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = QuestionActivity.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;

    private int volume;
    private boolean isNightMode;
    private boolean isMusicOFF;

    public QuestionActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext());

        volume = sharedPreferencesUtils.readIntData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_VOLUME);
        //Log.d(TAG, String.valueOf(volume));

        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);
        //Log.d(TAG, String.valueOf(isMusicOFF));

        isNightMode = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_NIGHT_MODE);
        //Log.d(TAG, String.valueOf(isNightMode));

        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);

        if (isMusicOFF){
            Log.d(TAG, "oncreate OFF");
            intent.setAction("OFF");
        } else{
            Log.d(TAG, "oncreate ON");
            intent.setAction("ON");
        }
        startService(intent);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_question);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        // Ferma la musica quando l'app va in background
        stopMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        // Ferma la musica quando l'app va in background
        //stopMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // Ripristina la musica quando l'app ritorna in primo piano
        resumeMusic();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // Assicurati che la musica parta all'avvio dell'attività
        resumeMusic();
    }

    private void stopMusic() {
        Intent intent = new Intent(this, MusicService.class);
        Log.d(TAG, "stopMusic");
        intent.setAction("OFF");
        startService(intent); // Ferma la musica
    }

    private void resumeMusic() {
        stopService(new Intent(this, MusicService.class));

        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);
        Log.d(TAG, String.valueOf(isMusicOFF));

        Intent intent = new Intent(this, MusicService.class);

        if (isMusicOFF){
            Log.d(TAG, "resumemusic OFF");
            intent.setAction("OFF");
        }
        else {
            Log.d(TAG, "resumemusic ON");
            intent.setAction("ON");
        }

        startService(intent);
    }
}