package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.CORRECT_ANSWER;
import static com.unimib.triviaducks.util.Constants.REASON;
import static com.unimib.triviaducks.util.Constants.WRONG_ANSWER;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.unimib.triviaducks.R;

public class GameNextQuestionDialog extends DialogFragment {

    Button nextBtn;
    GameFragment fragment;
    TextView dialog_title;
    String reason;
    String correctAnswer;

    public GameNextQuestionDialog() {}

    public GameNextQuestionDialog(GameFragment fragment) {
        this.fragment = fragment;
    }

    public static GameNextQuestionDialog newInstance() {
        return new GameNextQuestionDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_game_next_question, null);

        if (getArguments() != null) {
            reason = getArguments().getString(REASON, getContext().getString(R.string.correct_answer));
            correctAnswer = getArguments().getString(CORRECT_ANSWER, "");
        }

        builder.setView(view);
        nextBtn = view.findViewById(R.id.next);
        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);

        //risposta corretta
        TextView correctAnswerTextView = view.findViewById(R.id.correct_answer);
        TextView dialogQuestion = view.findViewById(R.id.dialog_question);
        if (!reason.equals(getContext().getString(R.string.correct_answer))) {
            correctAnswerTextView.setText(correctAnswer); // Mostra la risposta corretta
            correctAnswerTextView.setVisibility(View.VISIBLE); // Mostra la TextView
        } else {
            correctAnswerTextView.setVisibility(View.GONE); // Se hai risposto correttamente nascondi la TextView
            dialogQuestion.setVisibility(View.GONE);
        }

        nextBtn.setOnClickListener(v -> {
            fragment.nextBtnPressed();
            dismiss(); //chiude la finestra di dialogo
        });
        return builder.create();
    }
}