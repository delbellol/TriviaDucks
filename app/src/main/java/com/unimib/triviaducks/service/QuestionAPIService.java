package com.unimib.triviaducks.service;

import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_AMOUNT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_CATEGORY;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_DIFFICULTY;
import static com.unimib.triviaducks.util.Constants.TRIVIA_ENDPOINT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_PARAMETER_TYPE;

import com.unimib.triviaducks.model.QuestionAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionAPIService {
    // Annota il metodo come una richiesta GET all'endpoint specificato.
    @GET(TRIVIA_ENDPOINT)
    Call<QuestionAPIResponse> getQuestions( // Restituisce un oggetto Call con la risposta dell'API.
                                            @Query(TRIVIA_PARAMETER_AMOUNT) int amount, // Parametro "amount" per specificare il numero di domande.
                                            @Query(TRIVIA_PARAMETER_TYPE) String type,  // Parametro "type" per specificare il tipo di domande.
                                            @Query(TRIVIA_PARAMETER_CATEGORY) int category, //categoria
                                            @Query(TRIVIA_PARAMETER_DIFFICULTY) String difficulty //difficoltà
    );
}

