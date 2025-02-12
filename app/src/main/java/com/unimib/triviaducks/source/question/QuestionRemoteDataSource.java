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
    private static final String TAG = QuestionRemoteDataSource.class.getSimpleName();

    private final QuestionAPIService questionAPIService; // Servizio Retrofit per l'API delle domande.

    private MutableLiveData<Result> resultMutableLiveData;


    public QuestionRemoteDataSource() {
        this.questionAPIService = ServiceLocator.getInstance().getQuestionAPIService();
        resultMutableLiveData = new MutableLiveData<Result>();
    }


    @Override
    public void fetchQuestions(int category, int questionAmount, String difficulty) {
        Call<QuestionAPIResponse> questionResponseCall = questionAPIService.getQuestions(
                questionAmount, // Quantità di domande.
                TRIVIA_VALUE_TYPE,    // Tipo di domande.
                category,
                difficulty
        );

        // Esegue la chiamata in modo asincrono.
        questionResponseCall.enqueue(new Callback<QuestionAPIResponse>() {
            /**
             * Callback per la risposta positiva della chiamata Retrofit.
             */
            @Override
            public void onResponse(@NonNull Call<QuestionAPIResponse> call,
                                   @NonNull Response<QuestionAPIResponse> response) {
                // Verifica se la risposta è valida e contiene il codice di successo (responseCode == 0).
                if (response.body() != null &&
                        response.isSuccessful() &&
                        response.body().getResponseCode() == 0) {
                    // Notifica il successo al callback con la risposta e il timestamp corrente.
                    questionCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());
                    resultMutableLiveData.postValue(new Result.QuestionSuccess(response.body()));
                } else {
                    questionCallback.onFailureFromRemote(new Exception(ERROR_UNEXPECTED));
                }
            }

            /**
             * Callback per la risposta negativa (fallimento) della chiamata Retrofit.
             */
            @Override
            public void onFailure(@NonNull Call<QuestionAPIResponse> call, @NonNull Throwable t) {
                // Notifica il fallimento al callback con un messaggio di errore generico.
                questionCallback.onFailureFromRemote(new Exception(ERROR_RETROFIT));
            }
        });
    }
    public MutableLiveData<Result> getQuestions(){
        return resultMutableLiveData;
    }
}
