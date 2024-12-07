package com.unimib.triviaducks.repository;

public interface IQuestionRepository {

    void fetchQuestion(String question, int page, long lastUpdated);
}
