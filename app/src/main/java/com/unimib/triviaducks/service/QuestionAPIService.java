package com.unimib.triviaducks.service;

import static com.unimib.triviaducks.util.Constants.*;

import com.unimib.triviaducks.model.QuestionAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface QuestionAPIService {
    @GET (API_ENDPOINT)
    Call<QuestionAPIResponse> getQuestions(
            @Query(TOP_HEADLINES_AMOUNT_PARAMETER) String amount,
            @Query(TOP_HEADLINES_TYPE_PARAMETER) String type
    );
}
