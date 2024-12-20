package com.unimib.triviaducks.ui.game.fragment;

import static com.unimib.triviaducks.util.Constants.timerTime;
import static java.lang.String.*;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.QuestionRepository;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModel;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModelFactory;
import com.unimib.triviaducks.ui.home.fragment.GameModeFragment;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;
import com.unimib.triviaducks.util.TimerUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;


public class GameFragment extends Fragment {
    private static final String TAG = GameFragment.class.getSimpleName();

    private int counter = 0;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private Question currentQuestion;

    private QuestionRepository questionRepository;
    private List<Question> questionList;
    private QuestionViewModel questionViewModel;

    private CircularProgressIndicator circularProgressIndicator;
    private ConstraintLayout gameLayout;

    private TextView questionTextView, counterTextView, countdownTextView;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    private ImageButton closeImageButton;

    private final MutableLiveData<String> mutableQuestionCounter = new MutableLiveData<>();
    private final MutableLiveData<Long> mutableSecondsRemaining = new MutableLiveData<>();

    private CountDownTimer timer;
    private TimerUtils t;

    public GameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuestionRepository questionRepository =
                ServiceLocator.getInstance().getQuestionsRepository(
                        requireActivity().getApplication(),
                        requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );

        if (questionRepository == null) {
            Log.e(TAG, "QuestionRepository is null!");
        } else {
            questionViewModel = new ViewModelProvider(
                    requireActivity(),
                    new QuestionViewModelFactory(questionRepository)
            ).get(QuestionViewModel.class);

//            if (questionViewModel == null) {
//                Log.e(TAG, "questionViewModel is null!");
//            }
        }

        counter = 0;
        questionList = new ArrayList<>();
        t = new TimerUtils(this,this.getContext(),mutableSecondsRemaining);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);
        gameLayout = view.findViewById(R.id.gameLayout);

        closeImageButton = view.findViewById(R.id.close_game);

        counterTextView = view.findViewById(R.id.counter);

        questionTextView = view.findViewById(R.id.question);

        countdownTextView = view.findViewById(R.id.countdown);

        answerButton1 = view.findViewById(R.id.answer1);
        answerButton2 = view.findViewById(R.id.answer2);
        answerButton3 = view.findViewById(R.id.answer3);
        answerButton4 = view.findViewById(R.id.answer4);

        closeImageButton.setOnClickListener(v -> {
            GameQuitFragment gameQuitDialog = new GameQuitFragment();
            gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
        });

        View.OnClickListener answerClickListener = v -> {
            Button clickedButton = (Button) v;
            checkAnswer(clickedButton.getText().toString(), view);
        };

        answerButton1.setOnClickListener(answerClickListener);
        answerButton2.setOnClickListener(answerClickListener);
        answerButton3.setOnClickListener(answerClickListener);
        answerButton4.setOnClickListener(answerClickListener);


        questionViewModel.getQuestions(10, "multiple", System.currentTimeMillis()).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                        questionList.clear();
                        questionList.addAll(((Result.Success) result).getData().getQuestions());

                        circularProgressIndicator.setVisibility(View.GONE);
                        gameLayout.setVisibility(View.VISIBLE);

                        loadNextQuestion();
                    } else {
                        Snackbar.make(view, "Errore nel caricamento delle domande.", Snackbar.LENGTH_SHORT).show();
                    }
                });

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

    private void checkAnswer(String selectedAnswer, View view) {
        if (currentQuestion != null && selectedAnswer.equals(Jsoup.parse(currentQuestion.getCorrectAnswer()).text())) {
            Snackbar.make(view, "Risposta corretta!", Snackbar.LENGTH_SHORT).show();

            // Passa alla prossima domanda
            if (counter < questionList.size()) {
                loadNextQuestion();
            } else {
                // Fine delle domande
                Snackbar.make(view, "Hai completato il quiz!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, "Risposta sbagliata!", Snackbar.LENGTH_SHORT).show();
            t.endTimer();
            GameOverFragment gameOverDialog = new GameOverFragment(getString(R.string.wrong_answer));
            gameOverDialog.show(getParentFragmentManager(), "GameOverFragment");
        }
    }

    private void loadNextQuestion() {
        currentQuestion = questionList.get(counter);

        Log.d(TAG, Jsoup.parse(currentQuestion.getQuestion()).text());
        Log.d(TAG, Jsoup.parse(currentQuestion.getCorrectAnswer()).text());

        List<String> allAnswers = new ArrayList<>(currentQuestion.getIncorrectAnswers());
        allAnswers.add(currentQuestion.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        //Trasformato utilizzando i live data anziché l'assegnamento diretto,
        //in modo da facilitare lo spostamento della logica al di fuori del fragment
        mutableQuestionCounter.postValue(format("Domanda N. %d", counter + 1));


        questionTextView.setText(Jsoup.parse(currentQuestion.getQuestion()).text());

        answerButton1.setText(Jsoup.parse(allAnswers.get(0)).text());
        answerButton2.setText(Jsoup.parse(allAnswers.get(1)).text());
        answerButton3.setText(Jsoup.parse(allAnswers.get(2)).text());
        answerButton4.setText(Jsoup.parse(allAnswers.get(3)).text());


        t.startCountdown(timerTime);

        counter++;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Fa in modo che schiacciando indietro anziché
        //chiudere brutalmente mostri il gameQuitDialog
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Mostra il DialogFragment di conferma
                        GameQuitFragment gameQuitDialog = new GameQuitFragment();
                        gameQuitDialog.show(getParentFragmentManager(), "GameQuitFragment");
                    }
                });
    }
}