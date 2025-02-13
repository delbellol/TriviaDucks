package com.unimib.triviaducks.ui.game.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unimib.triviaducks.repository.question.QuestionRepository;

/**
 * Custom ViewModelProvider to be able to have a custom constructor
 * for the QuestionViewModel class.
 */
public class QuestionViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository; // Repository per le domande

    public QuestionViewModelFactory(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(questionRepository);
    }
}
