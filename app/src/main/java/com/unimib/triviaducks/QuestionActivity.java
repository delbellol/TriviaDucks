package com.unimib.triviaducks;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private CountDownTimer timer;
    private long timeRemaining = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        //bottone per uscire dalla partita
        ImageButton close = findViewById(R.id.close_game);
        close.setOnClickListener(view -> {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            showQuitGamePopup();
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
                // Logica quando il tempo finisce - DA FARE
            }
        }.start();
    }

    private void showQuitGamePopup() {
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
}