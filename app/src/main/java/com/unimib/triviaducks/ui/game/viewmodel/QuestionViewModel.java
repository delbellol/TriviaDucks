package com.unimib.triviaducks.ui.game.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.question.QuestionRepository;

// ViewModel che gestisce la logica delle domande e comunica con il repository
public class QuestionViewModel extends ViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();

    private final QuestionRepository questionRepository; // Repository per accedere ai dati delle domande
    private MutableLiveData<Result> questionListLiveData; // LiveData per osservare i dati delle domande


    public QuestionViewModel(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public MutableLiveData<Result> getQuestions() {
        return questionListLiveData;
    }

    public MutableLiveData<Result> fetchQuestions(int category, int questionAmount, String difficulty) {
        // Chiamata al repository per ottenere le domande e aggiornare questionListLiveData
        if (questionListLiveData == null) {
            questionListLiveData = questionRepository.fetchQuestions(category,questionAmount, difficulty);
        }
        return getQuestions();
    }
}