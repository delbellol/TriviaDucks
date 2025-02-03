package com.unimib.triviaducks.ui.game.viewmodel;

import static com.unimib.triviaducks.util.Constants.CORRECT_ANSWER;
import static com.unimib.triviaducks.util.Constants.EASY_QUESTION_POINTS;
import static com.unimib.triviaducks.util.Constants.END;
import static com.unimib.triviaducks.util.Constants.HARD_QUESTION_POINTS;
import static com.unimib.triviaducks.util.Constants.MEDIUM_QUESTION_POINTS;
import static com.unimib.triviaducks.util.Constants.QUIZ_FINISHED;
import static com.unimib.triviaducks.util.Constants.REASON;
import static com.unimib.triviaducks.util.Constants.SCORE;
import static com.unimib.triviaducks.util.Constants.TIMER_TIME;
import static com.unimib.triviaducks.util.Constants.TIME_EXPIRED;
import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_VALUE;
import static com.unimib.triviaducks.util.Constants.WRONG_ANSWER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.TimerUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class GameHandler {
    private static final String TAG = GameHandler.class.getSimpleName();

    private final GameFragment fragment;
    private final Context context;
    private final MutableLiveData<Long> mutableSecondsRemaining;
    private final MutableLiveData<String> mutableQuestionCounter;
    private final MutableLiveData<String> mutableScore;
    private QuestionViewModel questionViewModel;
    private List<Question> questionList;
    private int counter;
    private int totalCounter;
    private Question currentQuestion;
    private TimerUtils timerUtils;
    private int wrongAnswersCounter;
    private int score;
    private int category;

    public GameHandler(GameFragment fragment, Context context, MutableLiveData<Long> mutableSecondsRemaining, MutableLiveData<String> mutableQuestionCounter, MutableLiveData<String> mutableScore) {
        this.fragment = fragment;
        this.context = context;
        this.mutableSecondsRemaining = mutableSecondsRemaining;
        this.mutableQuestionCounter = mutableQuestionCounter;
        this.mutableScore = mutableScore;
        this.questionList = new ArrayList<>();
        this.counter = 0;
        this.wrongAnswersCounter = 0;

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

    public void loadQuestions(int category, int questionAmount, String difficulty) {
        fragment.showLoadingScreen();
        try {
            questionViewModel.fetchQuestions(category,questionAmount, difficulty).observe(fragment.getViewLifecycleOwner(), item -> {
                MutableLiveData<Result> rs = questionViewModel.getQuestions();
                if (Objects.requireNonNull(rs.getValue()).isSuccess()) {
                    List<Question> result = ((Result.QuestionSuccess)rs.getValue()).getData().getQuestions();
                    if (result != null) {
                        questionList.clear();
                        questionList.addAll(result);
                        loadNextQuestion();
                        fragment.hideLoadingScreen();
                    } else {
                        View view = fragment.getView();
                        if (view != null) {
                            Snackbar.make(view, "Errore nel caricamento delle domande.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }



    public void loadNextQuestion() {
        if (counter < questionList.size()) {
            new Thread(() -> {
                mutableQuestionCounter.postValue(String.format("Question N. %d", counter + 1));

                currentQuestion = questionList.get(counter);
                mutableScore.postValue(String.format("Difficulty: " + currentQuestion.getDifficulty()));

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
                    totalCounter++;
                    timerUtils.startCountdown(TIMER_TIME);
                });
            }).start();
        }
    }

    public void checkAnswer(String selectedAnswer, View view) {
        timerUtils.endTimer();
        String correctAnswer = Jsoup.parse(currentQuestion.getCorrectAnswer()).text();
        if (currentQuestion != null && selectedAnswer.equals(Jsoup.parse(currentQuestion.getCorrectAnswer()).text())) {
            //Snackbar.make(view, "Risposta corretta!", Snackbar.LENGTH_SHORT).show();
            if (counter < questionList.size()) {
                GameNextQuestionFragment nextQstDialog = new GameNextQuestionFragment((GameFragment) fragment, context.getString(R.string.correct_answer));
                timerUtils.endTimer();
                AddScore();
                nextQstDialog.show(fragment.getParentFragmentManager(), "GameNextQuestionFragment");
            }
            else {
//                GameOverFragment gameOverDialog = new GameOverFragment("Hai terminato il quiz",score,correctAnswer,true);
//                gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");
                GameOverFragment gameOverDialog = new GameOverFragment();
                Bundle bundle = new Bundle();
                bundle.putString(REASON,QUIZ_FINISHED);
                bundle.putInt(SCORE,score);
                bundle.putBoolean(END,true);
                gameOverDialog.setArguments(bundle);
                gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");
            }
        } else {
            wrongAnswersCounter++;
            fragment.handleWrongAnswer();
            //Snackbar.make(view, "Risposta sbagliata!", Snackbar.LENGTH_SHORT).show();
            if (wrongAnswersCounter >= 3){
                GameOverFragment gameOverDialog = new GameOverFragment();
                Bundle bundle = new Bundle();
                bundle.putString(REASON,WRONG_ANSWER);
                bundle.putInt(SCORE,score);
                bundle.putString(CORRECT_ANSWER,correctAnswer);
                bundle.putBoolean(END,false);
                gameOverDialog.setArguments(bundle);
                gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");

            }else{
                GameNextQuestionFragment nextQstDialog = new GameNextQuestionFragment(fragment, WRONG_ANSWER, correctAnswer);
                nextQstDialog.show(fragment.getParentFragmentManager(), "GameNextQuestionFragment");
            }

        }
    }

    private void AddScore() {
        String difficulty = currentQuestion.getDifficulty();
        switch (difficulty) {
            case "easy":
                score += EASY_QUESTION_POINTS;
                break;
            case "medium":
                score += MEDIUM_QUESTION_POINTS;
                break;
            case "hard":
                score += HARD_QUESTION_POINTS;
                break;
            default:
                Log.e(TAG,"Errore nell'ottenere la difficoltà della domanda");
                break;
        }
        Log.d(TAG, "Punteggio: "+score);
    }

    public void endGame() {
        timerUtils.endTimer();
    }

    public void handleTimerExpired() {
        wrongAnswersCounter++; // Incrementa il contatore degli errori
        fragment.handleWrongAnswer(); // Per togliere cuore rosso
        String correctAnswer = Jsoup.parse(currentQuestion.getCorrectAnswer()).text();
        if (wrongAnswersCounter >= 3) {
            GameOverFragment gameOverDialog = new GameOverFragment();
            Bundle bundle = new Bundle();
            bundle.putString(REASON,TIME_EXPIRED);
            bundle.putInt(SCORE,score);
            bundle.putString(CORRECT_ANSWER,correctAnswer);
            bundle.putBoolean(END,false);
            gameOverDialog.setArguments(bundle);
            gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");
        } else {
            GameNextQuestionFragment nextQstDialog = new GameNextQuestionFragment(
                    fragment, context.getString(R.string.time_expired), correctAnswer);
            nextQstDialog.show(fragment.getParentFragmentManager(), "GameNextQuestionFragment");
        }
    }


}
