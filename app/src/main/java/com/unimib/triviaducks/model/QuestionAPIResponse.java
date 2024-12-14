package com.unimib.triviaducks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionAPIResponse {
    @JsonProperty("response_code")
    private int responseCode;
    private List<Question> results;

    public QuestionAPIResponse() {
    }

    public QuestionAPIResponse(List<Question> results) {
        this.results = results;
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
