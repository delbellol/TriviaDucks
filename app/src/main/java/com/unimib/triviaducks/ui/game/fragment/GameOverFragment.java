package com.unimib.triviaducks.ui.game.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.HomeActivity;

public class GameOverFragment extends DialogFragment {

    Button home;
    TextView dialog_title;
    TextView scoreView;
    String reason;
    int score;
    String correctAnswer;

    public GameOverFragment() {
        reason = getString(R.string.wrong_answer);
    }
    public GameOverFragment(String reason, int score) {
        this.reason = reason;
        this.score = score;
    }

    public GameOverFragment(String reason, int score, String correctAnswer) {
        this.reason = reason;
        this.score = score;
        this.correctAnswer = correctAnswer;
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
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            dismiss(); //chiude la finestra di dialogo
        });

        dialog_title = view.findViewById(R.id.dialog_title);
        dialog_title.setText(reason);

        scoreView = view.findViewById(R.id.score);
        scoreView.setText("Score: "+score);

        TextView correctAnswerView = view.findViewById(R.id.correct_answer);
        // Mostra la risposta corretta, se disponibile
        if (correctAnswer != null && !correctAnswer.isEmpty()) {
            correctAnswerView.setText(correctAnswer);
            correctAnswerView.setVisibility(View.VISIBLE); // Rendi visibile la TextView
        }
        return builder.create();
    }
}