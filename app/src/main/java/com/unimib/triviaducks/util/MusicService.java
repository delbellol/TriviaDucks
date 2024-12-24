package com.unimib.triviaducks.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.unimib.triviaducks.R;

public class MusicService extends Service {
    private MediaPlayer music;

    @Override
    public void onCreate() {
        super.onCreate();
        music = MediaPlayer.create(this, R.raw.music);
        music.setLooping(true); // Ripeti la musica
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "ON":
                    if (!music.isPlaying()) {
                        music.start();
                    }
                    break;
                case "OFF":
                    if (music.isPlaying()) {
                        music.pause();
                    }
                    break;
                case "VOLUME":
                    // Ottieni il valore del volume dal SeekBar
                    int volume = intent.getIntExtra("Volume", 50); // Default volume: 50
                    // Calcola il volume da 0 a 1
                    float volumeLevel = volume / 100f;
                    // Imposta il volume del MediaPlayer (entrambi i canali: sinistro e destro)
                    music.setVolume(volumeLevel, volumeLevel);
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.stop();
            music.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
