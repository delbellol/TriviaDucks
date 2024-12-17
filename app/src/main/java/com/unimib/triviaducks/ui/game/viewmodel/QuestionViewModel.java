package com.unimib.triviaducks.ui.game.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.QuestionRepository;

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
    public MutableLiveData<Result> getQuestions(int amount, String type, long lastUpdate) {
        // Se questionListLiveData è null, esegui il fetch delle domande
        if (questionListLiveData == null) {
            fetchQuestions(amount, type, lastUpdate);
        }
        return questionListLiveData; // Ritorna la LiveData con le domande
    }

    // Metodo privato per chiamare il repository e ottenere le domande
    private void fetchQuestions(int amount, String type, long lastUpdate) {
        // Chiamata al repository per ottenere le domande e aggiornare questionListLiveData
        questionListLiveData = questionRepository.fetchQuestions(amount, type, lastUpdate);
    }

    // Metodo privato per ottenere le domande senza parametri (probabilmente non usato)
    private void getQuestions() {
        // Chiamata al repository per ottenere tutte le domande
        questionListLiveData = questionRepository.getQuestions();
    }
}