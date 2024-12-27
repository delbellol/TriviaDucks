package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.ui.home.fragment.GameModeFragment;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.ui.game.viewmodel.GameHandler;

import org.jsoup.Jsoup;

import java.util.List;

public class GameFragment extends Fragment {
    private static final String TAG = GameFragment.class.getSimpleName();

    private CircularProgressIndicator circularProgressIndicator;
    private ConstraintLayout gameLayout;

    private TextView questionTextView, counterTextView, countdownTextView;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    private ImageButton closeImageButton;

    private final MutableLiveData<String> mutableQuestionCounter = new MutableLiveData<>();
    private final MutableLiveData<Long> mutableSecondsRemaining = new MutableLiveData<>();

    private final MutableLiveData<String> questionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> answersLiveData = new MutableLiveData<>();

    private GameHandler gameHandler;

    public GameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameHandler = new GameHandler(this, this.getContext(), mutableSecondsRemaining, mutableQuestionCounter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        // Inizializza gli elementi della vista
        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);
        gameLayout = view.findViewById(R.id.gameLayout);

        closeImageButton = view.findViewById(R.id.close_game);

        counterTextView = view.findViewById(R.id.counter);

        questionTextView = view.findViewById(R.id.question);

        countdownTextView = view.findViewById(R.id.countdown);

        //TODO probabilmente per i bottoni delle risposte connviene utilizzare una recycler view/adapter
        answerButton1 = view.findViewById(R.id.answer1);
        answerButton2 = view.findViewById(R.id.answer2);
        answerButton3 = view.findViewById(R.id.answer3);
        answerButton4 = view.findViewById(R.id.answer4);

        // Gestisce l'evento di clic sul bottone per chiudere il gioco
        closeImageButton.setOnClickListener(v -> {
            GameQuitFragment gameQuitDialog = new GameQuitFragment(gameHandler);
            gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
        });

        // Gestisce l'evento di clic sui bottoni di risposta
        View.OnClickListener answerClickListener = v -> {
            Button clickedButton = (Button) v;
            disableAnswerButtons();
            gameHandler.checkAnswer(clickedButton.getText().toString(), view);
        };

        // Assegna l'ascoltatore agli altri bottoni di risposta
        answerButton1.setOnClickListener(answerClickListener);
        answerButton2.setOnClickListener(answerClickListener);
        answerButton3.setOnClickListener(answerClickListener);
        answerButton4.setOnClickListener(answerClickListener);

        gameHandler.loadQuestions(10, "multiple", System.currentTimeMillis());

        mutableSecondsRemaining.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long seconds) {
                countdownTextView.setText(String.valueOf(seconds));
            }
        });

        mutableQuestionCounter.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                counterTextView.setText(text);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        GameQuitFragment gameQuitDialog = new GameQuitFragment(gameHandler);
                        gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
                    }
                });
    }

    public void nextBtnPressed() {
        gameHandler.loadNextQuestion();
    }

    private void disableAnswerButtons() {
        answerButton1.setEnabled(false);
        answerButton2.setEnabled(false);
        answerButton3.setEnabled(false);
        answerButton4.setEnabled(false);
    }

    public void enableAnswerButtons() {
        answerButton1.setEnabled(true);
        answerButton2.setEnabled(true);
        answerButton3.setEnabled(true);
        answerButton4.setEnabled(true);
    }

    public void hideLoadingScreen(){
        circularProgressIndicator.setVisibility(View.GONE);
        gameLayout.setVisibility(View.VISIBLE);
    }

    public void setAnswerText(Question currentQuestion, List<String> allAnswers){
        questionTextView.setText(Jsoup.parse(currentQuestion.getQuestion()).text());

        // Imposta il testo per i bottoni delle risposte
        answerButton1.setText(Jsoup.parse(allAnswers.get(0)).text());
        answerButton2.setText(Jsoup.parse(allAnswers.get(1)).text());
        answerButton3.setText(Jsoup.parse(allAnswers.get(2)).text());
        answerButton4.setText(Jsoup.parse(allAnswers.get(3)).text());
    }
}
