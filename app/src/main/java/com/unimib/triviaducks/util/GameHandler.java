package com.unimib.triviaducks.util;

import static com.unimib.triviaducks.util.Constants.PARAMETER_CORRECT_ANSWER;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY_EASY;
import static com.unimib.triviaducks.util.Constants.QUESTION_POINTS_EASY;
import static com.unimib.triviaducks.util.Constants.PARAMETER_END;
import static com.unimib.triviaducks.util.Constants.ERROR_LOADING_QUESTIONS;
import static com.unimib.triviaducks.util.Constants.ERROR;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY_HARD;
import static com.unimib.triviaducks.util.Constants.QUESTION_POINTS_HARD;
import static com.unimib.triviaducks.util.Constants.DIFFICULTY_MEDIUM;
import static com.unimib.triviaducks.util.Constants.QUESTION_POINTS_MEDIUM;
import static com.unimib.triviaducks.util.Constants.TEXT_QUESTION_NUMBER;
import static com.unimib.triviaducks.util.Constants.ERROR_QUESTION_REPOSITORY_IS_NULL;
import static com.unimib.triviaducks.util.Constants.PARAMETER_QUIZ_FINISHED;
import static com.unimib.triviaducks.util.Constants.PARAMETER_REASON;
import static com.unimib.triviaducks.util.Constants.PARAMETER_SCORE;
import static com.unimib.triviaducks.util.Constants.TEXT_DIFFICULTY;
import static com.unimib.triviaducks.util.Constants.TIMER_TIME;
import static com.unimib.triviaducks.util.Constants.PARAMETER_TIME_EXPIRED;
import static com.unimib.triviaducks.util.Constants.WARNING_OBTAINING_DIFFICULTY;
import static com.unimib.triviaducks.util.Constants.PARAMETER_WRONG_ANSWER;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.question.QuestionRepository;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.ui.game.fragment.GameNextQuestionDialog;
import com.unimib.triviaducks.ui.game.fragment.GameOverDialog;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModel;
import com.unimib.triviaducks.ui.game.viewmodel.QuestionViewModelFactory;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// classe per la gestione del gioco
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
                        fragment.requireActivity().getApplication()
                );

        if (questionRepository == null) {
            Log.e(TAG, ERROR_QUESTION_REPOSITORY_IS_NULL);
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
                            Snackbar.make(view, ERROR_LOADING_QUESTIONS, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
            else ex.printStackTrace();
        }
    }

    public void loadNextQuestion() {
        if (counter < questionList.size()) {
            new Thread(() -> {
                mutableQuestionCounter.postValue(String.format(TEXT_QUESTION_NUMBER, counter + 1));

                currentQuestion = questionList.get(counter);
                mutableScore.postValue(String.format(TEXT_DIFFICULTY + currentQuestion.getDifficulty()));

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
            if (counter < questionList.size()) {
                GameNextQuestionDialog nextQstDialog = new GameNextQuestionDialog(fragment);
                Bundle bundle = new Bundle();
                bundle.putString(PARAMETER_REASON, context.getString(R.string.correct_answer));
                timerUtils.endTimer();
                AddScore();
                nextQstDialog.setArguments(bundle);
                nextQstDialog.show(fragment.getParentFragmentManager(), GameNextQuestionDialog.class.getSimpleName());
            }
            else {
                GameOverDialog gameOverDialog = new GameOverDialog();
                Bundle bundle = new Bundle();
                bundle.putString(PARAMETER_REASON, PARAMETER_QUIZ_FINISHED);
                bundle.putInt(PARAMETER_SCORE,score);
                bundle.putBoolean(PARAMETER_END,true);
                gameOverDialog.setArguments(bundle);
                gameOverDialog.show(fragment.getParentFragmentManager(), GameOverDialog.class.getSimpleName());
            }
        } else {
            wrongAnswersCounter++;
            fragment.handleWrongAnswer();
            Bundle bundle = new Bundle();
            if (wrongAnswersCounter >= 3){
                GameOverDialog gameOverDialog = new GameOverDialog();
                bundle.putString(PARAMETER_REASON, PARAMETER_WRONG_ANSWER);
                bundle.putInt(PARAMETER_SCORE,score);
                bundle.putString(PARAMETER_CORRECT_ANSWER,correctAnswer);
                bundle.putBoolean(PARAMETER_END,false);
                gameOverDialog.setArguments(bundle);
                gameOverDialog.show(fragment.getParentFragmentManager(), GameOverDialog.class.getSimpleName());

            }else if(counter >= questionList.size()) {
                GameOverDialog gameOverDialog = new GameOverDialog();
                bundle.putString(PARAMETER_REASON, PARAMETER_WRONG_ANSWER);
                bundle.putInt(PARAMETER_SCORE,score);
                bundle.putString(PARAMETER_CORRECT_ANSWER,correctAnswer);
                bundle.putBoolean(PARAMETER_END,true);
                gameOverDialog.setArguments(bundle);
                gameOverDialog.show(fragment.getParentFragmentManager(), GameOverDialog.class.getSimpleName());
            } else{
                GameNextQuestionDialog nextQstDialog = new GameNextQuestionDialog(fragment);
                bundle.putString(PARAMETER_REASON, PARAMETER_WRONG_ANSWER);
                bundle.putString(PARAMETER_CORRECT_ANSWER,correctAnswer);
                nextQstDialog.setArguments(bundle);
                nextQstDialog.show(fragment.getParentFragmentManager(), GameNextQuestionDialog.class.getSimpleName());
            }
        }
    }

    private void AddScore() {
        String difficulty = currentQuestion.getDifficulty();
        switch (difficulty) {
            case DIFFICULTY_EASY:
                score += QUESTION_POINTS_EASY;
                break;
            case DIFFICULTY_MEDIUM:
                score += QUESTION_POINTS_MEDIUM;
                break;
            case DIFFICULTY_HARD:
                score += QUESTION_POINTS_HARD;
                break;
            default:
                Log.w(TAG, WARNING_OBTAINING_DIFFICULTY);
                break;
        }
    }

    public void endGame() {
        timerUtils.endTimer();
    }

    public void handleTimerExpired() {
        wrongAnswersCounter++; // Incrementa il contatore degli errori
        fragment.handleWrongAnswer(); // Per togliere cuore rosso
        String correctAnswer = Jsoup.parse(currentQuestion.getCorrectAnswer()).text();
        Bundle bundle = new Bundle();
        if (wrongAnswersCounter >= 3) {
            GameOverDialog gameOverDialog = new GameOverDialog();
            bundle.putString(PARAMETER_REASON, PARAMETER_TIME_EXPIRED);
            bundle.putInt(PARAMETER_SCORE,score);
            bundle.putString(PARAMETER_CORRECT_ANSWER,correctAnswer);
            bundle.putBoolean(PARAMETER_END,false);
            gameOverDialog.setArguments(bundle);
            gameOverDialog.show(fragment.getParentFragmentManager(), GameOverDialog.class.getSimpleName());
        } else {
            GameNextQuestionDialog nextQstDialog = new GameNextQuestionDialog(fragment);
            bundle.putString(PARAMETER_REASON, PARAMETER_TIME_EXPIRED);
            bundle.putString(PARAMETER_CORRECT_ANSWER,correctAnswer);
            nextQstDialog.setArguments(bundle);
            nextQstDialog.show(fragment.getParentFragmentManager(), GameNextQuestionDialog.class.getSimpleName());
        }
    }


}
