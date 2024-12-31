package com.unimib.triviaducks.ui.game.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.MainActivity;

public class GameOverFragment extends DialogFragment {

    Button home;
    TextView dialog_title;
    String reason;

    public GameOverFragment() {
        reason = getString(R.string.wrong_answer);
    }
    public GameOverFragment(String reason) {
        this.reason = reason;
    }


    public static GameOverFragment newInstance() {
        return new GameOverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_over, null);
        builder.setView(view);
        home = view.findViewById(R.id.home);

        home.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.main_content);
            //Per spostarsi tra tra activity diverse a quanto pare bisogna usare questi intent maledetti
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            dismiss(); //chiude la finestra di dialogo
        });

        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);


        return builder.create();
    }
}