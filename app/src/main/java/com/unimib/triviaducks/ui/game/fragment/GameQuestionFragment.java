package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.GameViewModel;

import java.util.List;

public class GameQuestionFragment extends Fragment {
    private GameViewModel gameViewModel;
    private TextView questionTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    public GameQuestionFragment() {
        // Required empty public constructor
    }

    public static GameQuestionFragment newInstance() {
        GameQuestionFragment fragment = new GameQuestionFragment();
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
        return inflater.inflate(R.layout.fragment_game_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameViewModel = new GameViewModel();

        questionTextView = view.findViewById(R.id.question);
        //countdownTextView = view.findViewById(R.id.countdown);

        answer1Button = view.findViewById(R.id.answer1);
        answer2Button = view.findViewById(R.id.answer2);
        answer3Button = view.findViewById(R.id.answer3);
        answer4Button = view.findViewById(R.id.answer4);

        ImageButton closeImageButton = view.findViewById(R.id.close_game);

        //chiudere la partita quanndo premi bottone
        closeImageButton.setOnClickListener(v ->
                Log.d("QuestionActivity", "GAMEOVER")
        );

        //Richiamo metodi get per ottenere messaggi da visualizzare
        gameViewModel.setGameCallback(new GameViewModel.GameCallback() {
            @Override
            public void getQuestionData(String question, List<String> answers) {
                questionTextView.setText(question);
                updateAnswerButtons(answers);
            }
        });

        gameViewModel.quizDataTest();
    }

    //metodo che si occupa di accoppiare le risposte ai bottoni
    private void updateAnswerButtons(List<String> answers) {
        Button[] buttons = {answer1Button, answer2Button, answer3Button, answer4Button};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(answers.get(i));
            buttons[i].setOnClickListener(v -> gameViewModel.isCorrectAnswer());
        }
    }
}