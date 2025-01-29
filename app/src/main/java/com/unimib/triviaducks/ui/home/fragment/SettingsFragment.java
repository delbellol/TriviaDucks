package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_VOLUME;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.MusicService;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;

    private Switch musicSwitch;
    private Switch themeSwitch;
    private SeekBar volumeSeekBar;
    private int volume;
    private boolean isNightMode;
    private boolean isMusicOFF;


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

        sharedPreferencesUtils = new SharedPreferencesUtils(requireActivity().getApplication());

        musicSwitch = view.findViewById(R.id.music_switch);
        themeSwitch = view.findViewById(R.id.theme_switch);
        volumeSeekBar = view.findViewById(R.id.volume_seekbar);

        volume = sharedPreferencesUtils.readIntData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_VOLUME);

        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

        isNightMode = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_NIGHT_MODE);

        // Imposta lo stato iniziale dello switch music
        musicSwitch.setChecked(!isMusicOFF);

        // Imposta il volume iniziale
//        SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

//        sharedPreferencesUtils.writeIntData(
//                Constants.SHARED_PREFERENCES_FILENAME,
//                Constants.SHARED_PREFERENCES_VOLUME,
//                volume);  // Default volume: 50
        volumeSeekBar.setProgress(volume);

        // Imposta lo stato iniziale dello switch del tema
//        sharedPreferencesUtils.writeBooleanData(
//                Constants.SHARED_PREFERENCES_FILENAME,
//                Constants.SHARED_PREFERENCES_IS_NIGHT_MODE,
//                isNightMode);
        themeSwitch.setChecked(isNightMode);

        // Gestione del cambio stato dello switch muisc
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent intent = new Intent(requireContext(), MusicService.class);
            if (isChecked) {
                intent.setAction("ON");
                isMusicOFF = false;
            } else {
                intent.setAction("OFF");
                isMusicOFF = true;
            }
            requireContext().startService(intent);

            // Salva lo stato dell'interruttore
            sharedPreferencesUtils.writeBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF,
                    isMusicOFF);
            Log.d(TAG, String.valueOf(sharedPreferencesUtils.readBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF)));
        });

        // Gestione del cambio di tema tramite Switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isNightMode = true;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isNightMode = false;
            }

            // Salva lo stato dell'interruttore
            sharedPreferencesUtils.writeBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_NIGHT_MODE,
                    isNightMode);
        });

        // Gestione del cambiamento del volume tramite SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Salva il volume in SharedPreferences
                sharedPreferencesUtils.writeIntData(
                        Constants.SHARED_PREFERENCES_FILENAME,
                        SHARED_PREFERENCES_VOLUME,
                        progress);

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
}
