package com.unimib.triviaducks.model;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.triviaducks.Question;
import com.unimib.triviaducks.QuizData;
import com.unimib.triviaducks.R;

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
    private CountDownTimer timer;
    private long secondsRemaining = 0;
    private String currentQuestion;
    private List<String> currentAnswers;
    private int counter = 0;
    private Question currentResult;

    //TODO nomi da cambiare e sistemare codice
    //TODO andrannno aggiunti anche gli eventi per quanndo termina il tempo e per il gameover
    //Interfaccia che permette di notificare gli eventi al fragment
    public interface GameCallback {
        void getQuestionData(int counter, String question, List<String> answers);
        void timeOut(long secondsRemaining);
        void gameOver();
    }

    private GameCallback callback;

    public void setGameCallback(GameCallback callback) {
        this.callback = callback;
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

                mainHandler.post(() -> loadNewQuestion());
            } catch (Exception e) {
                Log.d("GameViewModel", "Errore: " + e.getMessage());
            }
        }).start();
    }

    //TODO cambiare con il gameover fatto bene
    //metodo che controlla se la risposta è corretta
    public void isCorrectAnswer (String currentAnswer) {
        String correctAnswer = quizData.getResults().get(counter - 1).getCorrectAnswer();
        if (quizData == null) {
            Log.d("GameViewModel", "Errore quizData è uguale a null");
        }
        else if (counter >= quizData.getResults().size()-1) {
            gameOver();
        }
        else if (currentAnswer.equals(correctAnswer)){
            loadNewQuestion();
        }
        else{
            gameOver();
        }
    }

    //TODO nomi da cambiare
    //metodo che si occupa di vedere se la risposta è corretta e di andare avanti o
    //dare il gameover
    private void loadNewQuestion () {
        currentResult = quizData.getResults().get(counter);
        currentQuestion = currentResult.getQuestion();
        Log.d("GameViewModel", currentQuestion);
        
        List<String> answers = new ArrayList<>();
        answers.add(currentResult.getCorrectAnswer());
        Log.d("GameViewModel", currentResult.getCorrectAnswer());
        answers.addAll(currentResult.getIncorrectAnswers());

        Collections.shuffle(answers);

        currentAnswers = answers;

        if (callback != null) {
            callback.getQuestionData(counter, currentQuestion, currentAnswers);
        }

        startCountdown(30999);

        counter++;
    }

    //TODO Aggiungere metodi per timer e metodi per gameover

    //metodo gameover
    private void gameOver() {
        Log.d("GameViewModel", "GAMEOVER");
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
                long secondsRemaining = millisUntilFinished / 1000;
                callback.timeOut(secondsRemaining);
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();
    }
}
