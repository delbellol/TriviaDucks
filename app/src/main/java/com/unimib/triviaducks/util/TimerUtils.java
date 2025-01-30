package com.unimib.triviaducks.util;
import static com.unimib.triviaducks.util.Constants.COUNTDOWN_INTERVAL;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.ui.game.fragment.GameOverFragment;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {

    private CountDownTimer timer;
    private GameFragment fragment;
    private Context context;
    private MutableLiveData<Long> mutableSecondsRemaining;
    private int score;


    public TimerUtils() {
        //required empty public constructor
    }
    public TimerUtils(GameFragment fragment, Context context, MutableLiveData<Long> mutableSecondsRemaining) {
        this.fragment = fragment;
        this.context = context;
        this.mutableSecondsRemaining = mutableSecondsRemaining;
    }

    //metodo countdown
    public void startCountdown(long duration) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mutableSecondsRemaining.postValue(millisUntilFinished / COUNTDOWN_INTERVAL);
            }

            @Override
            public void onFinish() {
                endTimer();
                showGameOver();
            }
        }.start();
    }

    public void endTimer() {
        if (timer != null) {
            this.score = fragment.getScore();
            timer.cancel();
            timer = null;
        }

    }

    private void showGameOver(){
        GameOverFragment gameOverDialog = new GameOverFragment(context.getString(R.string.time_expired),score);
        gameOverDialog.show(fragment.getParentFragmentManager(), "GameOverFragment");
    }
}
