package com.unimib.triviaducks.ui.game.viewmodel;

import static com.unimib.triviaducks.util.Constants.COUNTDOWN_INTERVAL;
import static com.unimib.triviaducks.util.Constants.FRESH_TIMEOUT;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.question.QuestionRepository;

import java.util.List;

// ViewModel che gestisce la logica delle domande e comunica con il repository
public class QuestionViewModel extends ViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName(); // Definisce il tag per il log

    private final QuestionRepository questionRepository; // Repository per accedere ai dati delle domande
    private MutableLiveData<Result> questionListLiveData; // LiveData per osservare i dati delle domande

    // Costruttore che riceve il repository per gestire la logica delle domande
    public QuestionViewModel(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Metodo per ottenere le domande, crea la LiveData se non esiste e la aggiorna
    public MutableLiveData<Result> getQuestions() {
        return questionListLiveData;
    }

    // Metodo privato per chiamare il repository e ottenere le domande
    public MutableLiveData<Result> fetchQuestions(int category) {
        // Chiamata al repository per ottenere le domande e aggiornare questionListLiveData
        if (questionListLiveData == null) {
            questionListLiveData = questionRepository.fetchQuestions(category);
        }
        return getQuestions();
    }



    // Metodo privato per ottenere le domande senza parametri (probabilmente non usato)
//    private MutableLiveData<Result> getQuestions() {
//        // Chiamata al repository per ottenere tutte le domande
//        return questionRepository.getQuestions();
//    }
}