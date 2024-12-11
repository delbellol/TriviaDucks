package com.unimib.triviaducks.repository;

import com.unimib.triviaducks.model.Question;

public interface IQuestionRepository {

    void fetchQuestions(int amount, String type, long lastUpdate);
    void updateQuestions(Question question);


}

