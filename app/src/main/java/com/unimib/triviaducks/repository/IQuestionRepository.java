package com.unimib.triviaducks.repository;

import com.unimib.triviaducks.model.Question;

public interface IQuestionRepository {

    void fetchQuestions(String amount, String type, long lastUpdate);
    void updateQuestions(Question question);


}

