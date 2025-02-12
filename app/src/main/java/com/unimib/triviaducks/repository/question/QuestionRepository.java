package com.unimib.triviaducks.repository.question;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.source.question.BaseQuestionRemoteDataSource;


public class QuestionRepository implements QuestionResponseCallback {

    private MutableLiveData<Result> allQuestionsMutableLiveData;
    private final BaseQuestionRemoteDataSource questionRemoteDataSource;

    public QuestionRepository(BaseQuestionRemoteDataSource questionRemoteDataSource) {
        allQuestionsMutableLiveData = new MutableLiveData<>();
        this.questionRemoteDataSource = questionRemoteDataSource;
        this.questionRemoteDataSource.setQuestionCallback(this);
    }

    public MutableLiveData<Result> fetchQuestions(int category, int questionAmount, String difficulty) {

        questionRemoteDataSource.fetchQuestions(category,questionAmount, difficulty);
        allQuestionsMutableLiveData = questionRemoteDataSource.getQuestions();
        return allQuestionsMutableLiveData;
    }

    public MutableLiveData<Result> getQuestions() {
        questionRemoteDataSource.getQuestions();
        return allQuestionsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allQuestionsMutableLiveData.postValue(result);
    }
}
