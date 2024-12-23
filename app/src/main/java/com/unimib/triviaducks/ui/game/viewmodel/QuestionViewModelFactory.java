package com.unimib.triviaducks.ui.game.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unimib.triviaducks.repository.QuestionRepository;

/**
 * Custom ViewModelProvider to be able to have a custom constructor
 * for the QuestionViewModel class.
 */
public class QuestionViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository; // Repository per le domande

    // Costruttore che riceve il repository e lo memorizza
    public QuestionViewModelFactory(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Metodo per creare una nuova istanza del ViewModel, utilizzando il costruttore personalizzato
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // Restituisce una nuova istanza di QuestionViewModel, passando il repository
        return (T) new QuestionViewModel(questionRepository);
    }
}
