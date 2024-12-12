package com.unimib.triviaducks.service;

import static com.unimib.triviaducks.util.Constants.TRIVIA_AMOUNT_PARAMETER;
import static com.unimib.triviaducks.util.Constants.TRIVIA_ENDPOINT;
import static com.unimib.triviaducks.util.Constants.TRIVIA_TYPE_PARAMETER;

import com.unimib.triviaducks.model.QuestionAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionAPIService {
    @GET(TRIVIA_ENDPOINT)
    Call<QuestionAPIResponse> getQuestions(
            @Query(TRIVIA_AMOUNT_PARAMETER) int amount,
            @Query(TRIVIA_TYPE_PARAMETER) String type
    );
}

