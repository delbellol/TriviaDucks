package com.unimib.triviaducks.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.MusicService;

public class SettingsFragment extends Fragment {

    private Switch musicSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        musicSwitch.setChecked(isMusicPlaying()); // Imposta lo stato iniziale dell'interruttore
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Avvia il servizio musicale
                Intent intent = new Intent(requireContext(), MusicService.class);
                intent.setAction("ON");
                requireContext().startService(intent);
            } else {
                // Ferma il servizio musicale
                Intent intent = new Intent(requireContext(), MusicService.class);
                intent.setAction("OFF");
                requireContext().startService(intent);
            }
            // Salva lo stato dell'interruttore
            SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("MusicEnabled", isChecked);
            editor.apply();
        });
    }

    // Metodo per verificare se la musica è in riproduzione (da personalizzare in base alla tua implementazione)
    private boolean isMusicPlaying() {
        SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getBoolean("MusicEnabled", false);
    }
}