package com.unimib.triviaducks;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private CountDownTimer timer;
    private long timeRemaining = 0;
    private QuizData answer;
    private int counter = 0;
    private TextView questionTextView;
    private Button correctButton, answerButton1, answerButton2, answerButton3, answerButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        questionTextView = findViewById(R.id.question);
        answerButton1 = findViewById(R.id.answer1);
        answerButton2 = findViewById(R.id.answer2);
        answerButton3 = findViewById(R.id.answer3);
        answerButton4 = findViewById(R.id.answer4);

        new Thread(() -> {
            try {
                onLoad();
                runOnUiThread(() -> {
                    Log.d("QuestionActivity", answer.toString());
                    isCorrectAnswer();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Log.d("QuestionActivity", "Errore: " + e.getMessage());});
            }
        }).start();


        //bottone per uscire dalla partita
        ImageButton close = findViewById(R.id.close_game);
        close.setOnClickListener(view -> {
            showQuitGameDialog();
        });

        //distinzione tra le due modalità
        String mode = getIntent().getStringExtra("mode");

        assert mode != null;
        if (mode.equals("oneshot")) {
            oneShot();
        }
        else{
            trials();
        }

        //Bottone risposta corretta
        answerButton1.setOnClickListener(view -> {
            isCorrectAnswer();
        });
    }

    private void onLoad () throws Exception{
        String result = "";

        URL url = new URL("https://opentdb.com/api.php?amount=10&type=multiple");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result += line;
            }
            jsonToObject(result);
        }
    }

    private void jsonToObject (String json) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        answer = objectMapper.readValue(json, QuizData.class);
    }

    // Metodi delle due modalità
    private void oneShot (){
        countdownTextView = findViewById(R.id.countdown);
        startCountdown(20000);
    }

    private void trials (){

    }

    //metodo del countdown
    private void startCountdown(long duration) {
        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                long seconds = millisUntilFinished / 1000;
                countdownTextView.setText(seconds % 60 + "");
            }

            @Override
            public void onFinish() {
                countdownTextView.setText(R.string.time_out);
                showGameOverDialog();
            }
        }.start();
    }

    //metodo del dialog che chiede conferma prima di uscire dalla partita
    private void showQuitGameDialog() {
        if (isFinishing() || isDestroyed())
            return;

        if (timer != null) {
            timer.cancel();
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_quit);
        dialog.setCancelable(false);

        Button quitGameButton = dialog.findViewById(R.id.quit_game);
        Button cancelButton = dialog.findViewById(R.id.cancel);

        quitGameButton.setOnClickListener(view -> {
            finish();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(view -> {
            String mode = getIntent().getStringExtra("mode");
            assert mode != null;
            if (mode.equals("oneshot"))
                startCountdown(timeRemaining);

            dialog.dismiss();
        });

        dialog.show();
    }

    //metodo game over
    private void showGameOverDialog() {
        if (isFinishing() || isDestroyed())
            return;

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        dialog.setCancelable(false);

        Button backHomeButton = dialog.findViewById(R.id.home);
        backHomeButton.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }

    //metodo per risposta corretta
    private void isCorrectAnswer () {
        List<String> answers = new ArrayList<>();
        answers.add(answer.getResults().get(counter).getCorrectAnswer());
        answers.add(answer.getResults().get(counter).getIncorrectAnswers().get(0));
        answers.add(answer.getResults().get(counter).getIncorrectAnswers().get(1));
        answers.add(answer.getResults().get(counter).getIncorrectAnswers().get(2));
        List<Button> buttons = new ArrayList<>();
        buttons.add(answerButton1);
        buttons.add(answerButton2);
        buttons.add(answerButton3);
        buttons.add(answerButton4);

        //Log.d("Question_activity", answers+" ");

        Collections.shuffle(answers);

        questionTextView.setText(answer.getResults().get(counter).getQuestion());

        for (int i=0; i<answers.size(); i++) {
            if (answers.get(i).equals(answer.getResults().get(counter).getCorrectAnswer())) {
                Log.d("Question_activity", "test");
                //Aggiungere qui la parte per cambiare il bottone della risposta corretta
            }
            buttons.get(i).setText(answers.get(i));
        }
        Log.d("Question_activity", answers+" ");

        if (counter < answer.getResults().size()-1)
            counter++;
        else {
            showGameOverDialog();
        }
    }
}