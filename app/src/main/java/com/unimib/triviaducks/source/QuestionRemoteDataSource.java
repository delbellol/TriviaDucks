package com.unimib.triviaducks.source;

import static com.unimib.triviaducks.util.Constants.RETROFIT_ERROR;
import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_VALUE;
import static com.unimib.triviaducks.util.Constants.TRIVIA_TYPE_VALUE;
import static com.unimib.triviaducks.util.Constants.UNEXPECTED_ERROR;

import androidx.annotation.NonNull;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.util.ServiceLocator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionRemoteDataSource extends BaseQuestionRemoteDataSource{
    private final QuestionAPIService questionAPIService;

    public QuestionRemoteDataSource() {
        this.questionAPIService = ServiceLocator.getInstance().getQuestionAPIService();
    }

    @Override
    public void getQuestions() {
        Call<QuestionAPIResponse> newsResponseCall = questionAPIService.getQuestions(TRIVIA_AMOUNT_VALUE,
                TRIVIA_TYPE_VALUE);

        newsResponseCall.enqueue(new Callback<QuestionAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionAPIResponse> call,
                                   @NonNull Response<QuestionAPIResponse> response) {

                if (response.body() != null && response.isSuccessful() &&
                        response.body().getResponseCode() == 0) {
                    questionCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    //TODO Cambiare tipo di errore
                    questionCallback.onFailureFromRemote(new Exception(UNEXPECTED_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionAPIResponse> call, @NonNull Throwable t) {
                questionCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }
}
