package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.unimib.triviaducks.R;
import com.unimib.triviaducks.util.GameController;

import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameFragment extends Fragment {
    private GameController gameController;
    private TextView questionTextView, counterTextView, countdownTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (requireActivity().getResources().getBoolean(R.bool.debug_mode)) {
            questionRepository = new QuestionMockRepository(requireActivity().getApplication(), this);
        } else {
            questionRepository = new QuestionAPIRepository(requireActivity().getApplication(), this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_question, container, false);
    }

    /*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameViewModel = new GameController(); //Aggiunto getContext perché serve per JSONParserUtils


        questionTextView = view.findViewById(R.id.question);
        counterTextView = view.findViewById(R.id.counter);
        countdownTextView = view.findViewById(R.id.countdown);

        answer1Button = view.findViewById(R.id.answer1);
        answer2Button = view.findViewById(R.id.answer2);
        answer3Button = view.findViewById(R.id.answer3);
        answer4Button = view.findViewById(R.id.answer4);

        //chiudere la partita quando premi bottone
        ImageButton closeImageButton = view.findViewById(R.id.close_game);
        closeImageButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_gameQuestionFragment_to_gameQuitFragment)
        );

        //Richiamo metodi per ottenere messaggi da visualizzare
        gameViewModel.questionCounter.observe(getViewLifecycleOwner(), counter -> {
            counterTextView.setText(counter);
        });

        gameViewModel.currentQuestion.observe(getViewLifecycleOwner(), question -> {
            //implementando jsoup decodifico i caratteri speciali, come ad esempio ", nel testo
            //della domanda
            questionTextView.setText(Jsoup.parse(question).text());
        });

        gameViewModel.currentAnswers.observe(getViewLifecycleOwner(), answers -> {
            List<Button> buttons = Arrays.asList(answer1Button, answer2Button, answer3Button, answer4Button);
            Collections.shuffle(buttons);

            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);
                String answer = answers.get(i);

                //implementando jsoup decodifico i caratteri speciali nel testo delle risposte
                button.setText(Jsoup.parse(answer).text());
                button.setOnClickListener(v -> gameViewModel.isCorrectAnswer(answer));
            }
        });

        gameViewModel.secondsRemaining.observe(getViewLifecycleOwner(), seconds -> {
            countdownTextView.setText(String.valueOf(seconds));
        });

        gameViewModel.gameOver.observe(getViewLifecycleOwner(), isGameOver -> {
            if (isGameOver) {
                // Mostra dialog di game over
                //lo mostra come fragmennt
                //Navigation.findNavController(getActivity(), R.id.main_content).navigate(R.id.action_gameQuestionFragment_to_gameOverFragment);
                //lo mostra come dialog
                GameOverFragment gameOverDialog = new GameOverFragment();
                gameOverDialog.show(getParentFragmentManager(), "gameOverDialog"); //changed getFragmentManager (deprecated) with getParentFragmentManager
            }
        });

        gameViewModel.quizDataTest();
    }
     */
}