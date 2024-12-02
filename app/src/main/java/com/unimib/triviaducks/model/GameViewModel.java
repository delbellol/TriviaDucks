package com.unimib.triviaducks.model;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.triviaducks.Question;
import com.unimib.triviaducks.QuizData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameViewModel extends ViewModel {
    private QuizData quizData;
    private CountDownTimer timer;
    private int counter = 0;
    private Question currentResult;

    //LiveData per gestire le informazioni
    private final MutableLiveData<String> mutableQuestionCounter = new MutableLiveData<>();
    public LiveData<String> questionCounter = mutableQuestionCounter;

    private final MutableLiveData<String> mutableCurrentQuestion = new MutableLiveData<>();
    public LiveData<String> currentQuestion = mutableCurrentQuestion;

    private final MutableLiveData<List<String>> mutableCurrentAnswers = new MutableLiveData<>();
    public LiveData<List<String>> currentAnswers = mutableCurrentAnswers;

    private final MutableLiveData<Long> mutableSecondsRemaining = new MutableLiveData<>();
    public LiveData<Long> secondsRemaining = mutableSecondsRemaining;

    private final MutableLiveData<Boolean> mutableGameOver = new MutableLiveData<>();
    public LiveData<Boolean> gameOver = mutableGameOver;

    //TODO nomi da cambiare
    //TODO rendere metodo private
    //Rende i dati della chiamata API oggetti java di tipo QuizData, contiene il codice
    //che era implementato direttamente nel onViewCreated e il metodo jsonToObject
    public void quizDataTest() {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                String jsonResponse = apiCall();

                ObjectMapper objectMapper = new ObjectMapper();
                quizData = objectMapper.readValue(jsonResponse, QuizData.class);

                mainHandler.post(() -> loadNewQuestion());
            } catch (Exception e) {
                Log.d("GameViewModel", "Errore: " + e.getMessage());
            }
        }).start();
    }

    //TODO nomi da cambiare
    //Fa una chiamata di tipo GET per ottennere i dati dalla API
    private String apiCall() throws IOException {
        StringBuilder result = new StringBuilder();

        URL url = new URL("https://opentdb.com/api.php?amount=10&type=multiple");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }

        return result.toString();
    }

    //TODO cambiare con il gameover fatto bene
    //metodo che controlla se la risposta è corretta
    public void isCorrectAnswer (String currentAnswer) {
        String correctAnswer = quizData.getResults().get(counter - 1).getCorrectAnswer();
        if (quizData == null) {
            Log.d("GameViewModel", "Errore quizData è uguale a null");
        }
        else if (counter >= quizData.getResults().size()-1) {
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
        currentResult = quizData.getResults().get(counter);

        List<String> answers = new ArrayList<>();
        answers.add(currentResult.getCorrectAnswer());
        answers.addAll(currentResult.getIncorrectAnswers());

        Log.d("GameViewModel", currentResult.getQuestion());
        Log.d("GameViewModel", currentResult.getCorrectAnswer());

        Collections.shuffle(answers);

        mutableQuestionCounter.postValue(String.valueOf(counter+1));
        mutableCurrentQuestion.postValue(currentResult.getQuestion());
        mutableCurrentAnswers.postValue(answers);

        //TODO renndere 30999 unna constannte nella classe util.connstannts
        // forse annche 1000 nel metodo startcountdown
        startCountdown(30999);

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
        mutableGameOver.postValue(true);
    }

    //TODO Renderlo private e sistemare nomi e sistemarlo
    //metodo countdown
    public void startCountdown(long duration) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mutableSecondsRemaining.postValue(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }
}
