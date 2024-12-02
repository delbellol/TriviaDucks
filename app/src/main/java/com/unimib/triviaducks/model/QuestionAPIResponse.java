package com.unimib.triviaducks.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionAPIResponse {
    @JsonProperty("response_code")
    private int responseCode;
    private List<Question> results;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public List<Question> getResults() {
        return results;
    }

    public void setResults(List<Question> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public String toString() {
        String AAA = "";
        for (int i=0; i<results.size(); i++) {
            AAA+= results.get(i).toString()+"\n";
        }
        return AAA;
    }
}
