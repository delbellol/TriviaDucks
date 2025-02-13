package com.unimib.triviaducks.util;
import static com.unimib.triviaducks.util.Constants.TIMER_COUNTDOWN_INTERVAL;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.ui.game.fragment.GameFragment;

public class TimerUtils {

    private CountDownTimer timer;
    private GameFragment fragment;
    private Context context;
    private MutableLiveData<Long> mutableSecondsRemaining;
    private int score;


    public TimerUtils() {}

    public TimerUtils(GameFragment fragment, Context context, MutableLiveData<Long> mutableSecondsRemaining) {
        this.fragment = fragment;
        this.context = context;
        this.mutableSecondsRemaining = mutableSecondsRemaining;
    }

    public void startCountdown(long duration) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(duration, TIMER_COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mutableSecondsRemaining.postValue(millisUntilFinished / TIMER_COUNTDOWN_INTERVAL);
            }

            @Override
            public void onFinish() {
                endTimer();
                notifyTimerExpired();
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

    public void notifyTimerExpired() {
            fragment.handleTimerExpired();
    }
}
