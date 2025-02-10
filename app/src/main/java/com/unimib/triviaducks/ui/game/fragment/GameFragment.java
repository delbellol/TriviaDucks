package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.WARNING_CONNECTION;
import static com.unimib.triviaducks.util.Constants.PARAMETER_CAN_PLAY;

import android.content.Intent;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_AMOUNT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_CATEGORY;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_DIFFICULTY;

import android.os.Bundle;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.ui.connection.ConnectionErrorActivity;
import com.unimib.triviaducks.util.GameHandler;
import com.unimib.triviaducks.util.NetworkUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class GameFragment extends Fragment {
    private static final String TAG = GameFragment.class.getSimpleName();

    private CircularProgressIndicator circularProgressIndicator;
    private ConstraintLayout gameLayout;

    private TextView questionTextView, counterTextView, countdownTextView, difficultyTextView;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    private ImageButton closeImageButton;

    private final MutableLiveData<String> mutableQuestionCounter = new MutableLiveData<>();
    private final MutableLiveData<Long> mutableSecondsRemaining = new MutableLiveData<>();
    private final MutableLiveData<String> mutableScore = new MutableLiveData<>();

    private GameHandler gameHandler;
    private LottieAnimationView lottieHeart1, lottieHeart2, lottieHeart3;

    private int category; //categoria delle domande da passare al GameHandler
    private String difficulty;

    private int errorsCount = 0;
    private int score;
    private int questionAmount;

    private static boolean canPlay = false; //Variabile per impedire che il gioco possa iniziare se è già finito

    public GameFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameHandler = new GameHandler(this, this.getContext(), mutableSecondsRemaining, mutableQuestionCounter, mutableScore);

        category = getActivity().getIntent().getIntExtra(TRIVIA_PARAMETER_CATEGORY,0);
        difficulty = getActivity().getIntent().getStringExtra(TRIVIA_PARAMETER_DIFFICULTY);
        category = getActivity().getIntent().getIntExtra(TRIVIA_PARAMETER_CATEGORY,0);
        questionAmount = getActivity().getIntent().getIntExtra(TRIVIA_PARAMETER_AMOUNT,10);
        canPlay = getActivity().getIntent().getBooleanExtra(PARAMETER_CAN_PLAY,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (canPlay) {
            // Inizializza gli elementi della vista
            circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);
            gameLayout = view.findViewById(R.id.gameLayout);

            closeImageButton = view.findViewById(R.id.close_game);

            counterTextView = view.findViewById(R.id.counter);

            questionTextView = view.findViewById(R.id.question);

            countdownTextView = view.findViewById(R.id.countdown);

            lottieHeart1 = view.findViewById(R.id.lottie_heart1);
            lottieHeart2 = view.findViewById(R.id.lottie_heart2);
            lottieHeart3 = view.findViewById(R.id.lottie_heart3);

            answerButton1 = view.findViewById(R.id.answer1);
            answerButton2 = view.findViewById(R.id.answer2);
            answerButton3 = view.findViewById(R.id.answer3);
            answerButton4 = view.findViewById(R.id.answer4);

            // Gestisce l'evento di clic sul bottone per chiudere il gioco
            closeImageButton.setOnClickListener(v -> {
                GameQuitDialog gameQuitDialog = new GameQuitDialog(gameHandler);
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

            if (!NetworkUtil.isInternetAvailable(getContext())) {
                Intent intent = new Intent(getActivity(), ConnectionErrorActivity.class);
                startActivity(intent);
            }
            else gameHandler.loadQuestions(category,questionAmount, difficulty);

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

            difficultyTextView = view.findViewById(R.id.difficulty);

            mutableScore.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String text) {
                    difficultyTextView.setText(text);
                }
            });

            canPlay = false;
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        GameQuitDialog gameQuitDialog = new GameQuitDialog(gameHandler);
                        gameQuitDialog.show(getParentFragmentManager(), GameQuitDialog.class.getSimpleName());
                    }
                });
    }

    public void nextBtnPressed() {
        if (!NetworkUtil.isInternetAvailable(getContext())) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    WARNING_CONNECTION,
                    Snackbar.LENGTH_LONG).show();
        }

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

    public void showLoadingScreen() {
        gameLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void setAnswerText(Question currentQuestion, List<String> allAnswers){
        questionTextView.setText(Jsoup.parse(currentQuestion.getQuestion()).text());

        // Imposta il testo per i bottoni delle risposte
        answerButton1.setText(Jsoup.parse(allAnswers.get(0)).text());
        answerButton2.setText(Jsoup.parse(allAnswers.get(1)).text());
        answerButton3.setText(Jsoup.parse(allAnswers.get(2)).text());
        answerButton4.setText(Jsoup.parse(allAnswers.get(3)).text());
    }

    public void handleWrongAnswer() {
        errorsCount++; // Incrementa il numero di errori
        if (errorsCount == 1) {
            lottieHeart3.setAnimation(R.raw.heart_black);
            lottieHeart3.playAnimation();
        } else if (errorsCount == 2) {
            lottieHeart2.setAnimation(R.raw.heart_black);
            lottieHeart2.playAnimation();
        } else if (errorsCount == 3) {
            lottieHeart1.setAnimation(R.raw.heart_black);
            lottieHeart1.playAnimation();
        }
    }

    public void handleTimerExpired() {
        gameHandler.handleTimerExpired();
    }

    public int getScore() {
        return score;
    }

    public static void setCanPlay(boolean value) {
        canPlay = value;
    }

}
