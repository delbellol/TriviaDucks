package com.unimib.triviaducks.repository;

import static com.unimib.triviaducks.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.source.BaseQuestionLocalDataSource;
import com.unimib.triviaducks.source.BaseQuestionRemoteDataSource;
import com.unimib.triviaducks.source.QuestionCallback;

import java.util.List;

public class QuestionRepository implements QuestionCallback {

    private static final String TAG = QuestionRepository.class.getSimpleName();

    private final MutableLiveData<Result> allQuestionsMutableLiveData;
    private final BaseQuestionRemoteDataSource questionRemoteDataSource;
    private final BaseQuestionLocalDataSource questionLocalDataSource;

    public QuestionRepository(BaseQuestionRemoteDataSource questionRemoteDataSource,
                              BaseQuestionLocalDataSource questionLocalDataSource) {
        allQuestionsMutableLiveData = new MutableLiveData<>();
        this.questionRemoteDataSource = questionRemoteDataSource;
        this.questionLocalDataSource = questionLocalDataSource;
        this.questionRemoteDataSource.setQuestionCallback(this);
        this.questionLocalDataSource.setQuestionCallback(this);
    }

    public MutableLiveData<Result> fetchQuestions(int amount, String type, long lastUpdate) {
        long currentTime = System.currentTimeMillis();
        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago

        //TODO sistemare if
        //Log.d(TAG, String.valueOf((currentTime - lastUpdate > FRESH_TIMEOUT)));
        //if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            questionRemoteDataSource.getQuestions();
        //} else {
        //    questionLocalDataSource.getQuestions();
        //}

        return allQuestionsMutableLiveData;
    }

    public MutableLiveData<Result> getQuestions() {
        questionLocalDataSource.getQuestions();
        return allQuestionsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate) {
        questionLocalDataSource.insertQuestions(questionAPIResponse.getQuestions());
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allQuestionsMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(List<Question> questionList) {
        Result.Success result = new Result.Success(new QuestionAPIResponse(questionList));
        allQuestionsMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allQuestionsMutableLiveData.postValue(resultError);
    }
}
