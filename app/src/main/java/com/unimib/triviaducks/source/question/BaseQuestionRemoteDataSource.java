package com.unimib.triviaducks.source.question;

import com.unimib.triviaducks.repository.question.QuestionResponseCallback;

public abstract class BaseQuestionRemoteDataSource {
    protected QuestionResponseCallback questionCallback;

    public void setQuestionCallback(QuestionResponseCallback questionCallback) {
        this.questionCallback = questionCallback;
    }

    public abstract void getQuestions();
}
