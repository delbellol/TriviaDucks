package com.unimib.triviaducks.ui.game.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.QuestionRepository;

public class QuestionViewModel extends ViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();

    private final QuestionRepository questionRepository;
    private MutableLiveData<Result> questionListLiveData;

    public QuestionViewModel(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public MutableLiveData<Result> getQuestions(int amount, String type, long lastUpdate) {
        if (questionListLiveData == null) {
            fetchQuestions(amount, type, lastUpdate);
        }
        return questionListLiveData;
    }

    private void fetchQuestions(int amount, String type, long lastUpdate) {
        questionListLiveData = questionRepository.fetchQuestions(amount, type, lastUpdate);
    }

    private void getQuestions() {
        questionListLiveData = questionRepository.getQuestions();
    }
}
