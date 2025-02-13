package com.unimib.triviaducks.ui.connection;

import static com.unimib.triviaducks.util.Constants.ERROR;
import static com.unimib.triviaducks.util.Constants.VALUE_OFF;
import static com.unimib.triviaducks.util.Constants.VALUE_ON;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.connection.fragment.ConnectionErrorFragment;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.MusicService;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class ConnectionErrorActivity extends AppCompatActivity {
    private static final String TAG = com.unimib.triviaducks.ui.connection.ConnectionErrorActivity.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;
    private boolean isMusicOFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext());

            isMusicOFF = sharedPreferencesUtils.readBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);


            Intent intent = new Intent(this, MusicService.class);

            if (isMusicOFF)
                intent.setAction(VALUE_OFF);
            else
                intent.setAction(VALUE_ON);
            startService(intent);

            setContentView(R.layout.activity_error);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_connection_error, new ConnectionErrorFragment())
                        .commit();
            }
        }catch (Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
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
        resumeMusic();
    }

    private void stopMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(VALUE_OFF);
        startService(intent);
    }

    private void resumeMusic() {
        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

        Intent intent = new Intent(this, MusicService.class);

        if (isMusicOFF)
            intent.setAction(VALUE_OFF);
        else
            intent.setAction(VALUE_ON);

        startService(intent);
    }
}

