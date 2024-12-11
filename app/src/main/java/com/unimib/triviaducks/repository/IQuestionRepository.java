package com.unimib.triviaducks.repository;

import com.unimib.triviaducks.model.Question;

public interface IQuestionRepository {
    void fetchQuestion(int amount, String type, long lastUpdate);

    void updateQuestion(Question question);

}
