package com.unimib.triviaducks.source.question;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.question.QuestionResponseCallback;

public abstract class BaseQuestionRemoteDataSource {
    protected QuestionResponseCallback questionCallback;

    public void setQuestionCallback(QuestionResponseCallback questionCallback) {
        this.questionCallback = questionCallback;
    }

    public abstract MutableLiveData<Result> getQuestions();
    public abstract void fetchQuestions(int category, int questionAmount, String difficulty);
}
