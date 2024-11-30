package com.unimib.triviaducks.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

public class GameViewModel {
    private QuizData quizData;
    private long timeRemaining;
    private String currentQuestion;
    private List<String> currentAnswers;
    private int counter = 0;
    private Question currentResult;

    //TODO nomi da cambiare e sistemare codice
    //TODO andrannno aggiunti anche gli eventi per quanndo termina il tempo e per il gameover
    //Interfaccia che permette di notificare gli eventi al fragment
    public interface GameCallback {
        void getQuestionData(String question, List<String> answers);
    }

    private GameCallback callback;

    public void setGameCallback(GameCallback callback) {
        this.callback = callback;
    }

    //TODO nomi da cambiare
    //Fa una chiamata di tipo GET per ottennere i dati dalla API
    private String apiCall() throws IOException {
        String result = "";

        URL url = new URL("https://opentdb.com/api.php?amount=10&type=multiple");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result += line;
            }
        }

        return result;
    }

    //TODO nomi da cambiare
    //TODO rendere metodo private
    //TODO per ora usiamo handler ma è meglio utlizzare livedata
    //Rende i dati della chiamata API oggetti java di tipo QuizData, contiene il codice
    //che era implementato direttamente nel onViewCreated e il metodo jsonToObject
    public void quizDataTest() {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                String jsonResponse = apiCall();

                ObjectMapper objectMapper = new ObjectMapper();
                quizData = objectMapper.readValue(jsonResponse, QuizData.class);

                mainHandler.post(() -> {
                    isCorrectAnswer();
                });
            } catch (Exception e) {
                Log.d("GameViewModel", "Errore: " + e.getMessage());
            }
        }).start();
    }



    //TODO nomi da cambiare
    //TODO mettere private il metodo
    //TODO fare in modo che se premi il bottone sbagliato dà gameover
    //TODO Sistemare Index 10 out of bounds for length 10
    //metodo che si occupa di vedere se la risposta è corretta e di andare avanti o
    //dare il gameover
    public void isCorrectAnswer () {
        if (quizData == null)
            Log.d("GameViewModel", "Errore quizData è uguale a null");

        //TODO cambiare con il gameover fatto bene
        if (counter >= quizData.getResults().size()-1)
            Log.d("GameViewModel", "GAMEOVER");

        currentResult = quizData.getResults().get(counter);
        currentQuestion = currentResult.getQuestion();
        
        List<String> answers = new ArrayList<>();
        answers.add(currentResult.getCorrectAnswer());
        Log.d("GameViewModel", ""+currentResult.getCorrectAnswer());
        answers.addAll(currentResult.getIncorrectAnswers());

        Collections.shuffle(answers);

        currentAnswers = answers;

        //Log.d("GameViewModel", ""+ currentQuestion + currentAnswers);
        if (callback != null) {
            callback.getQuestionData(currentQuestion, currentAnswers);
        }

        counter++;
    }

    //TODO Aggiungere metodi per timer e metodi per gameover
}
