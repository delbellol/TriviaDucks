package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.triviaducks.QuizData;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.QuestionActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameQuestionFragment extends Fragment {
    private TextView countdownTextView;
    private CountDownTimer timer;
    private long timeRemaining = 0;
    private QuizData answer;
    private int counter = 0;
    private TextView questionTextView;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;

    public GameQuestionFragment() {
        // Required empty public constructor
    }

    public static GameQuestionFragment newInstance(String param1, String param2) {
        GameQuestionFragment fragment = new GameQuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionTextView = view.findViewById(R.id.question);
        answerButton1 = view.findViewById(R.id.answer1);
        answerButton2 = view.findViewById(R.id.answer2);
        answerButton3 = view.findViewById(R.id.answer3);
        answerButton4 = view.findViewById(R.id.answer4);

        //TODO Se utilizzi librerie come ViewModel e LiveData, potresti anche considerare di
        // spostare questo tipo di operazioni logiche nel ViewModel, mantenendo il codice del
        // frammento più pulito e conforme all'architettura MVVM.
        new Thread(() -> {
            try {
                onLoad();
                new Handler(Looper.getMainLooper()).post(() -> {
                    Log.d("QuestionActivity", answer.toString());
                    isCorrectAnswer();
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Log.d("QuestionActivity", "Errore: " + e.getMessage())
                );
            }
        }).start();



        //bottone per uscire dalla partita
        ImageButton close = view.findViewById(R.id.close_game);
        close.setOnClickListener(v ->
                //showQuitGameDialog()
                Log.d("QuestionActivity", "GAMEOVER")
        );

        /*distinzione tra le due modalità
        String mode = getIntent().getStringExtra("mode");

        assert mode != null;
        if (mode.equals("oneshot")) {
            oneShot();
        }
        else{
            trials();
        }*/
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

    private void jsonToObject (String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        answer = objectMapper.readValue(json, QuizData.class);
    }

    // Metodi delle due modalità
    private void oneShot (){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        countdownTextView.findViewById(R.id.countdown);
        startCountdown(20999);
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
                //showGameOverDialog();
            }
        }.start();
    }

    //metodo del dialog che chiede conferma prima di uscire dalla partita
    /*
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
            if (timer != null)
                timer.cancel();
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
    */

    //metodo game over
    /*
    private void showGameOverDialog() {
        if (isFinishing() || isDestroyed())
            return;

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        dialog.setCancelable(false);

        Button backHomeButton = dialog.findViewById(R.id.home);
        backHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });
        dialog.show();
    }
    */

    //metodo per risposta corretta
    private void isCorrectAnswer () {
        if (counter == answer.getResults().size()-1) {
            //showGameOverDialog();
            Log.d("QuestionActivity", "GAMEOVER");
        }
        else {
            //String mode = getIntent().getStringExtra("mode");
            String mode = "trials";
            assert mode != null;

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

            Collections.shuffle(answers);

            questionTextView.setText(answer.getResults().get(counter).getQuestion());

            for (int i = 0; i < answers.size(); i++) {
                buttons.get(i).setText(answers.get(i));
                if (answers.get(i).equals(answer.getResults().get(counter).getCorrectAnswer())) {
                    if (mode.equals("oneshot")) {
                        oneShot();
                    }
                    buttons.get(i).setOnClickListener(v -> isCorrectAnswer());
                    counter++;
                } else {
                    //buttons.get(i).setOnClickListener(v -> showGameOverDialog());
                    Log.d("QuestionActivity", "GAMEOVER");
                }
            }
        }
    }
}