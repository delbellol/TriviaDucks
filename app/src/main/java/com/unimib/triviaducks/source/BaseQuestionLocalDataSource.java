package com.unimib.triviaducks.source;

import com.unimib.triviaducks.model.Question;

import java.util.List;

public abstract class BaseQuestionLocalDataSource {
    protected QuestionCallback questionCallback;

    public void setQuestionCallback(QuestionCallback questionCallback) {
        this.questionCallback = questionCallback;
    }

    public abstract void getQuestions();

    public abstract void updateQuestion(Question question);

    public abstract void deleteQuestions();

    public abstract void insertQuestions(List<Question> questionList);
}
