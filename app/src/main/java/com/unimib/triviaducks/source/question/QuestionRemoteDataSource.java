package com.unimib.triviaducks.source.question;

import static com.unimib.triviaducks.util.Constants.ERROR_RETROFIT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_VALUE_TYPE;
import static com.unimib.triviaducks.util.Constants.ERROR_UNEXPECTED;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.util.ServiceLocator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionRemoteDataSource extends BaseQuestionRemoteDataSource {

    private final QuestionAPIService questionAPIService;

    private MutableLiveData<Result> resultMutableLiveData;


    public QuestionRemoteDataSource() {
        this.questionAPIService = ServiceLocator.getInstance().getQuestionAPIService();
        resultMutableLiveData = new MutableLiveData<Result>();
    }


    @Override
    public void fetchQuestions(int category, int questionAmount, String difficulty) {
        Call<QuestionAPIResponse> questionResponseCall = questionAPIService.getQuestions(
                questionAmount,
                TRIVIA_VALUE_TYPE,
                category,
                difficulty
        );


        questionResponseCall.enqueue(new Callback<QuestionAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionAPIResponse> call,
                                   @NonNull Response<QuestionAPIResponse> response) {
                if (response.body() != null &&
                        response.isSuccessful() &&
                        response.body().getResponseCode() == 0) {
                    questionCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());
                    resultMutableLiveData.postValue(new Result.QuestionSuccess(response.body()));
                } else {
                    questionCallback.onFailureFromRemote(new Exception(ERROR_UNEXPECTED));
                }
            }


            @Override
            public void onFailure(@NonNull Call<QuestionAPIResponse> call, @NonNull Throwable t) {
                questionCallback.onFailureFromRemote(new Exception(ERROR_RETROFIT));
            }
        });
    }
    public MutableLiveData<Result> getQuestions(){
        return resultMutableLiveData;
    }
}
