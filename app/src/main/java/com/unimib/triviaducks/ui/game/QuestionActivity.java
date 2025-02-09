package com.unimib.triviaducks.ui.game;


import static com.unimib.triviaducks.util.Constants.GENERIC_ERROR;
import static com.unimib.triviaducks.util.Constants.MUSIC_OFF;
import static com.unimib.triviaducks.util.Constants.MUSIC_ON;

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
    private boolean isMusicOFF;

    public QuestionActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        GameFragment.setCanPlay(false);

        try {

            sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext());

            isMusicOFF = sharedPreferencesUtils.readBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

            Intent intent = new Intent(this, MusicService.class);
            stopService(intent);

            if (isMusicOFF) {
                intent.setAction(MUSIC_OFF);
            } else {
                intent.setAction(MUSIC_ON);
            }
            startService(intent);
        }catch (Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,GENERIC_ERROR+ex.getMessage());
            else ex.printStackTrace();
        }

        setContentView(R.layout.activity_question);
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
        intent.setAction(MUSIC_OFF);
        startService(intent); // Ferma la musica
    }

    private void resumeMusic() {
        stopService(new Intent(this, MusicService.class));

        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

        Intent intent = new Intent(this, MusicService.class);

        if (isMusicOFF){
            intent.setAction(MUSIC_OFF);
        }
        else {
            intent.setAction(MUSIC_ON);
        }

        startService(intent);
    }
}