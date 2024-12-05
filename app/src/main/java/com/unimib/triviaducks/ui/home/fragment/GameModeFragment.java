package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimib.triviaducks.R;

public class GameModeFragment extends Fragment {
    private Button play;

    public GameModeFragment() {
        // Required empty public constructor
    }

    public static GameModeFragment newInstance(String param1, String param2) {
        GameModeFragment fragment = new GameModeFragment();
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
        return inflater.inflate(R.layout.fragment_game_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            navController.navigate(R.id.action_gameModeFragment_to_questionActivity);
        });
    }
}