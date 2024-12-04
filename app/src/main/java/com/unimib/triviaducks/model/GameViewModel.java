package com.unimib.triviaducks.model;

import static com.unimib.triviaducks.util.Constants.countDownInterval;
import static com.unimib.triviaducks.util.Constants.timerTime;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.util.JSONParserUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameViewModel extends ViewModel {
    private QuestionAPIResponse questionAPIResponse;
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

    //Aggiunto context perché serve per richiamare JSONParserUtils
    //public Context context;
    public GameViewModel() {

    }

    //TODO nomi da cambiare
    //TODO rendere metodo private
    //Rende i dati della chiamata API oggetti java di tipo QuizData, contiene il codice
    //che era implementato direttamente nel onViewCreated e il metodo jsonToObject
    public void quizDataTest() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                String jsonResponse = apiCall();

                //Righe vecchie di Jackson, sarebbero da eliminare
                /*ObjectMapper objectMapper = new ObjectMapper();
                questionAPIResponse = objectMapper.readValue(jsonResponse, QuestionAPIResponse.class);
                 */
                //Creo un oggetto parser che parsa il JSON in oggetti passandogli il contesto
                JSONParserUtils parser = new JSONParserUtils();
                //Faccio una chiamata alla funzione parser.parse che parsa il json in una lista di oggetti che poi ritorna
                questionAPIResponse = parser.parseJSONWithGSon(jsonResponse);
                //Passo al main handler la lista di oggetti per tirarla fuori dal thread perché altrimenti al termine si distrugge
                mainHandler.post(() -> questionAPIResponse = questionAPIResponse);
                //Passo al main handler il compito di chiamare loadNewQuestion()
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

        mutableQuestionCounter.postValue(String.valueOf(counter+1));
        mutableCurrentQuestion.postValue(currentResult.getQuestion());
        mutableCurrentAnswers.postValue(answers);

        //DONE rendere 30999 unna constannte nella classe util.connstannts
        //DO forse annche 1000 nel metodo startcountdown
        startCountdown(timerTime);


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
    private void startCountdown(long duration) {  //con metodo private sembrerebbe funzionare
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mutableSecondsRemaining.postValue(millisUntilFinished / countDownInterval);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }
}
