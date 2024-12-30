package com.unimib.triviaducks.ui.game.viewmodel;

import static com.unimib.triviaducks.util.Constants.TIMER_TIME;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.question.QuestionRepository;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.ui.game.fragment.GameNextQuestionFragment;
import com.unimib.triviaducks.ui.game.fragment.GameOverFragment;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModel;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModelFactory;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.TimerUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private static final String TAG = GameHandler.class.getSimpleName();

    private final GameFragment fragment;
    private final Context context;
    private final MutableLiveData<Long> mutableSecondsRemaining;
    private final MutableLiveData<String> mutableQuestionCounter;
    private QuestionViewModel questionViewModel;
    private List<Question> questionList;
    private int counter;
    private Question currentQuestion;
    private TimerUtils timerUtils;

    public GameHandler(GameFragment fragment, Context context, MutableLiveData<Long> mutableSecondsRemaining, MutableLiveData<String> mutableQuestionCounter) {
        this.fragment = fragment;
        this.context = context;
        this.mutableSecondsRemaining = mutableSecondsRemaining;
        this.mutableQuestionCounter = mutableQuestionCounter;
        this.questionList = new ArrayList<>();
        this.counter = 0;

        QuestionRepository questionRepository =
                ServiceLocator.getInstance().getQuestionsRepository(
                        fragment.requireActivity().getApplication(),
                        fragment.requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );

        if (questionRepository == null) {
            Log.e(TAG, "QuestionRepository is null!");
        } else {
            questionViewModel = new ViewModelProvider(
                    fragment.requireActivity(),
                    new QuestionViewModelFactory(questionRepository)
            ).get(QuestionViewModel.class);
        }
        timerUtils = new TimerUtils(fragment, context, mutableSecondsRemaining);
    }

    public void loadQuestions(int numberOfQuestions, String type, long seed) {
        questionViewModel.getQuestions(numberOfQuestions, type, seed).observe(fragment.getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                questionList.clear();
                questionList.addAll(((Result.QuestionSuccess) result).getData().getQuestions());
                loadNextQuestion();
                fragment.hideLoadingScreen();
            } else {
                View view = fragment.getView();
                if (view != null) {
                    Snackbar.make(view, "Errore nel caricamento delle domande.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadNextQuestion() {
        if (counter < questionList.size()) {
            new Thread(() -> {
                mutableQuestionCounter.postValue(String.format("Domanda N. %d", counter + 1));

                currentQuestion = questionList.get(counter);
                Log.d(TAG, Jsoup.parse(currentQuestion.getQuestion()).text());
                Log.d(TAG, Jsoup.parse(currentQuestion.getCorrectAnswer()).text());

                List<String> allAnswers = new ArrayList<>(currentQuestion.getIncorrectAnswers());
                allAnswers.add(currentQuestion.getCorrectAnswer());
                Collections.shuffle(allAnswers);

                Looper.prepare();
                fragment.getActivity().runOnUiThread(() -> {
                    fragment.setAnswerText(currentQuestion, allAnswers);
                    fragment.enableAnswerButtons();
                    counter++;
                    timerUtils.startCountdown(TIMER_TIME);
                });
            }).start();
        }
    }

    public void checkAnswer(String selectedAnswer, View view) {
        if (currentQuestion != null && selectedAnswer.equals(Jsoup.parse(currentQuestion.getCorrectAnswer()).text())) {
            //Snackbar.make(view, "Risposta corretta!", Snackbar.LENGTH_SHORT).show();
            if (counter < questionList.size()) {
                timerUtils.endTimer();
                GameNextQuestionFragment nextQstDialog = new GameNextQuestionFragment((GameFragment) fragment);
                nextQstDialog.show(fragment.getParentFragmentManager(), "GameNextQuestionFragment");
            } else {
                Snackbar.make(view, "Hai completato il quiz!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            //Snackbar.make(view, "Risposta sbagliata!", Snackbar.LENGTH_SHORT).show();
            endGame();
            GameOverFragment gameOverDialog = new GameOverFragment(context.getString(R.string.wrong_answer));
            gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");
        }
    }

    public void endGame() {
        timerUtils.endTimer();
    }

}
