package com.unimib.triviaducks.ui.game.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.QuestionRepository;

public class QuestionViewModel extends ViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();

    private final QuestionRepository questionRepository;
    private MutableLiveData<Result> questionsListLiveData;

    public QuestionViewModel(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public MutableLiveData<Result> getQuestions(int amount, String type, long lastUpdate) {
        if (questionsListLiveData == null)
            fetchQuestions(amount, type, lastUpdate);
        return questionsListLiveData;
    }

    private void fetchQuestions(int amount, String type, long lastUpdate) {
        questionsListLiveData = questionRepository.fetchQuestions(amount, type, lastUpdate);
    }

    private void getQuestions() {
        questionsListLiveData = questionRepository.getQuestions();
    }
}
