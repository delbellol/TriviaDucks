package com.unimib.triviaducks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionAPIResponse {
    @SerializedName("response_code")
    private int responseCode;
    private List<Question> results;

    public QuestionAPIResponse() {
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<Question> getQuestions() {
        return results;
    }

    public void setQuestions(List<Question> results) {
        this.results = results;
    }
}
