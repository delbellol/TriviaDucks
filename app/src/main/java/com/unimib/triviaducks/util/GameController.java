package com.unimib.triviaducks.util;

import static com.unimib.triviaducks.util.Constants.COUNTDOWN_INTERVAL;
import static com.unimib.triviaducks.util.Constants.TIMER_TIME;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {
    private QuestionAPIResponse questionAPIResponse;
    private CountDownTimer timer;
    private int counter = 0;
    private Question currentResult;
    public long secondsRemaining;
    public boolean gameOver;


    //Aggiunto context perché serve per richiamare JSONParserUtils
    //public Context context;
    public GameController() {

    }

    //TODO cambiare con il gameover fatto bene
    //metodo che controlla se la risposta è corretta
    public void isCorrectAnswer (String currentAnswer) {
        String correctAnswer = questionAPIResponse.getResults().get(counter - 1).getCorrectAnswer();
        if (questionAPIResponse == null) {
            Log.d("GameViewModel", "Errore quizData è uguale a null");
        }
        else if (counter >= questionAPIResponse.getResults().size()-1) {
            endGame();
        }
        else if (currentAnswer.equals(correctAnswer)){
            loadNewQuestion();
        }
        else{
            endGame();
        }
    }

    //TODO nomi da cambiare
    //metodo che si occupa di vedere se la risposta è corretta e di andare avanti o
    //dare il gameover
    private void loadNewQuestion () {
        currentResult = questionAPIResponse.getResults().get(counter);

        List<String> answers = new ArrayList<>();
        answers.add(currentResult.getCorrectAnswer());
        answers.addAll(currentResult.getIncorrectAnswers());

        Log.d("GameViewModel", currentResult.getQuestion());
        Log.d("GameViewModel", currentResult.getCorrectAnswer());

        Collections.shuffle(answers);

        //mutableQuestionCounter.postValue(String.valueOf(counter+1));
        //mutableCurrentQuestion.postValue(currentResult.getQuestion());
        //mutableCurrentAnswers.postValue(answers);

        startCountdown(TIMER_TIME);

        counter++;
    }

    //TODO Aggiungere metodi per timer e metodi per gameover
    //metodo gameover
    private void endGame() {
        Log.d("GameViewModel", "GAMEOVER");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        //mutableGameOver.postValue(true);
    }

    //TODO Renderlo private e sistemare nomi e sistemarlo
    //metodo countdown
    private void startCountdown(long duration) {  //con metodo private sembrerebbe funzionare
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                //mutableSecondsRemaining.postValue(millisUntilFinished / COUNTDOWN_INTERVAL);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }
}
