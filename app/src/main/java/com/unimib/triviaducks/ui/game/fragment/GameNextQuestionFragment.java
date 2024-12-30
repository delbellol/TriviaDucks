package com.unimib.triviaducks.ui.game.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.unimib.triviaducks.R;

public class GameNextQuestionFragment extends DialogFragment {

    Button nextBtn;

    GameFragment fragment;
    TextView dialog_title;

    public GameNextQuestionFragment() {
        // Costruttore vuoto
    }

    public GameNextQuestionFragment(GameFragment fragment) {
        this.fragment = fragment;
    }

    public static GameNextQuestionFragment newInstance() {
        return new GameNextQuestionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_next_question, null);
        builder.setView(view);
        nextBtn = view.findViewById(R.id.next);

        nextBtn.setOnClickListener(v -> {
            fragment.nextBtnPressed();
            dismiss(); //chiude la finestra di dialogo
        });

        dialog_title = view.findViewById(R.id.dialog_title);



        return builder.create();
    }
}