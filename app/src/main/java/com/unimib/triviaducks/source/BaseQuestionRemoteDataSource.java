package com.unimib.triviaducks.source;

public abstract class BaseQuestionRemoteDataSource {
    protected QuestionCallback questionCallback;

    public void setQuestionCallback(QuestionCallback questionCallback) {
        this.questionCallback = questionCallback;
    }

    public abstract void getQuestions();
}
