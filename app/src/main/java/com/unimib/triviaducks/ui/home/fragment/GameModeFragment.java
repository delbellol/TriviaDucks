package com.unimib.triviaducks.ui.home.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.home.MainActivity;

public class GameModeFragment extends DialogFragment {
    private Button play;
    private Button close;

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

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_mode, container, false);
    }*/

    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_mode, null);
        builder.setView(view);

        play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            //navController.navigate(R.id.action_gameModeFragment_to_questionActivity);
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            startActivity(intent);
            dismiss(); //chiude la finestra di dialogo
        });

        close = view.findViewById(R.id.close);
        close.setOnClickListener(v ->
                //navController.navigate(R.id.action_gameModeFragment_to_homeFragment)
                dismiss() //chiude la finestra di dialogo
        );

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}