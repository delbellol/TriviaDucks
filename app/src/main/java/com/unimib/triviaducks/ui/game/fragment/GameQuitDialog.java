package com.unimib.triviaducks.ui.game.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.util.GameHandler;


public class GameQuitDialog extends DialogFragment {

    private Button close;
    private Button cancel;

    private GameHandler gameHandler;

    public GameQuitDialog() {}

    public GameQuitDialog(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public static GameQuitDialog newInstance() {
        return new GameQuitDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_game_quit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close = view.findViewById(R.id.close);
        close.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.main_content);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            gameHandler.endGame();
            this.dismiss();
            }
        );

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> this.dismiss());
    }
}