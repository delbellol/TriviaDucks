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

public class QuestionActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private CountDownTimer timer;
    private long timeRemaining = 0;
    private QuizData answer;
    private int counter = 0;
    private TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        questionTextView = findViewById(R.id.question);

        new Thread(() -> {
            try {
                onLoad();
                runOnUiThread(() -> {
                    Log.d("QuestionActivity", answer.toString());
                    if (counter < answer.getResults().size())
                        questionTextView.setText(answer.getResults().get(counter).getQuestion());
                    else
                        showGameOverDialog();
                    counter++;
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
        Button answer1 = findViewById(R.id.answer1);
        answer1.setOnClickListener(view -> {
            isCorrectAnswer();
        });
    }

    private void onLoad () throws Exception{
        String result = "";

        URL url = new URL("https://opentdb.com/api.php?amount=10");
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
        });

        cancelButton.setOnClickListener(view -> {
            dialog.dismiss();


            String mode = getIntent().getStringExtra("mode");
            assert mode != null;
            if (mode.equals("oneshot"))
                startCountdown(timeRemaining);
        });

        dialog.show();
    }

    //metodo game over
    private void showGameOverDialog() {
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
        counter++;
            questionTextView.setText(answer.getResults().get(counter).getQuestion());
        Log.d("Question_activity", ""+counter);
    }
}