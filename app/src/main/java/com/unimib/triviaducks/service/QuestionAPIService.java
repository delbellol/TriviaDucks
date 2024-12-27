package com.unimib.triviaducks.service;

import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_PARAMETER;
import static com.unimib.triviaducks.util.Constants.TRIVIA_ENDPOINT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_TYPE_PARAMETER;

import com.unimib.triviaducks.model.QuestionAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionAPIService {
    // Annota il metodo come una richiesta GET all'endpoint specificato.
    @GET(TRIVIA_ENDPOINT)
    Call<QuestionAPIResponse> getQuestions( // Restituisce un oggetto Call con la risposta dell'API.
                                            @Query(TRIVIA_AMOUNT_PARAMETER) int amount, // Parametro "amount" per specificare il numero di domande.
                                            @Query(TRIVIA_TYPE_PARAMETER) String type,  // Parametro "type" per specificare il tipo di domande.
                                            @Query("category") int category //categoria
                                            //TODO aggiunngere category
    );
}

