package com.unimib.triviaducks.source.question;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.repository.question.QuestionResponseCallback;

import java.util.List;

public abstract class BaseQuestionLocalDataSource {
    protected QuestionResponseCallback questionCallback;

    public void setQuestionCallback(QuestionResponseCallback questionCallback) {
        this.questionCallback = questionCallback;
    }

    public abstract void getQuestions();

    public abstract void updateQuestion(Question question);

    public abstract void deleteQuestions();

    public abstract void insertQuestions(List<Question> questionList);
}
