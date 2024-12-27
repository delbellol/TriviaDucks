package com.unimib.triviaducks.source;

import static com.unimib.triviaducks.util.Constants.RETROFIT_ERROR;
import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_VALUE;
import static com.unimib.triviaducks.util.Constants.TRIVIA_TYPE_VALUE;
import static com.unimib.triviaducks.util.Constants.UNEXPECTED_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.service.QuestionAPIService;
import com.unimib.triviaducks.ui.game.fragment.GameFragment;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import org.jsoup.Jsoup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe per recuperare le domande da una sorgente remota utilizzando Retrofit.
 */
public class QuestionRemoteDataSource extends BaseQuestionRemoteDataSource {
    private static final String TAG = QuestionRemoteDataSource.class.getSimpleName(); // Tag per il logging.

    private final QuestionAPIService questionAPIService; // Servizio Retrofit per l'API delle domande.

    /**
     * Costruttore: ottiene un'istanza del servizio Retrofit tramite ServiceLocator.
     */
    public QuestionRemoteDataSource() {
        this.questionAPIService = ServiceLocator.getInstance().getQuestionAPIService();
    }

    /**
     * Metodo per recuperare le domande dall'API remota.
     */
    @Override
    public void getQuestions() {
        // Crea una chiamata Retrofit per ottenere le domande.
        Call<QuestionAPIResponse> questionResponseCall = questionAPIService.getQuestions(
                TRIVIA_AMOUNT_VALUE, // Quantità di domande.
                TRIVIA_TYPE_VALUE,    // Tipo di domande.
                SharedPreferencesUtils.getCategory() //TODO capire come togliere quando è 0
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
                } else {
                    // TODO: Cambiare tipo di errore specifico se necessario.
                    questionCallback.onFailureFromRemote(new Exception(UNEXPECTED_ERROR));
                }
            }

            /**
             * Callback per la risposta negativa (fallimento) della chiamata Retrofit.
             */
            @Override
            public void onFailure(@NonNull Call<QuestionAPIResponse> call, @NonNull Throwable t) {
                // Notifica il fallimento al callback con un messaggio di errore generico.
                questionCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }
}
