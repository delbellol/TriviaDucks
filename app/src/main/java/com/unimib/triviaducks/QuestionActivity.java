package com.unimib.triviaducks;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuestionActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        //bottone per uscire dalla partita
        ImageButton close = findViewById(R.id.close_game);
        close.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //distinzione tra le due modalità
        String mode = getIntent().getStringExtra("mode");

        if (mode.equals("oneshot")) {
            oneShot();
        }
        else{
            trials();
        }
    }

    // Metodi delle due modalità
    private void oneShot (){
        countdownTextView = findViewById(R.id.countdown);
        startCountdown();
    }

    private void trials (){

    }

    //metodo del countdown
    private void startCountdown() {
        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                countdownTextView.setText(seconds % 60 + "");
            }

            @Override
            public void onFinish() {
                countdownTextView.setText(R.string.time_out);
                // Logica quando il tempo finisce - DA FARE
            }
        }.start();
    }
}