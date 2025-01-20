package com.unimib.triviaducks.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.MusicService;

public class SettingsFragment extends Fragment {

    private Switch musicSwitch;
    private Switch themeSwitch;
    private SeekBar volumeSeekBar;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        musicSwitch = view.findViewById(R.id.music_switch);
        themeSwitch = view.findViewById(R.id.theme_switch);
        volumeSeekBar = view.findViewById(R.id.volume_seekbar);

        // Imposta lo stato iniziale dello switch music
        boolean isMusicEnabled = isMusicPlaying();
        musicSwitch.setChecked(isMusicEnabled);

        // Imposta il volume iniziale
        SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int volume = preferences.getInt("Volume", 50);  // Default volume: 50
        volumeSeekBar.setProgress(volume);

        // Imposta lo stato iniziale dello switch del tema
        boolean isNightMode = preferences.getBoolean("ThemeNightMode", false);
        themeSwitch.setChecked(isNightMode);

        // Gestione del cambio stato dello switch muisc
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent intent = new Intent(requireContext(), MusicService.class);
            if (isChecked) {
                intent.setAction("ON");
            } else {
                intent.setAction("OFF");
            }
            requireContext().startService(intent);

            // Salva lo stato dell'interruttore
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("MusicEnabled", isChecked);
            editor.apply();
        });

        // Gestione del cambio di tema tramite Switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("ThemeNightMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("ThemeNightMode", false);
            }
            editor.apply();
        });

        // Gestione del cambiamento del volume tramite SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Salva il volume in SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Volume", progress);
                editor.apply();

                // Invia il volume al MusicService
                Intent intent = new Intent(requireContext(), MusicService.class);
                intent.setAction("VOLUME");
                intent.putExtra("Volume", progress); // Passa il volume tramite Intent
                requireContext().startService(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Metodo per verificare se la musica è in riproduzione
    private boolean isMusicPlaying() {
        SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getBoolean("MusicEnabled", true); // Se non esiste, restituisce "true" di default
    }
}
