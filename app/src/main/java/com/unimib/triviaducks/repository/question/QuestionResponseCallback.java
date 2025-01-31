package com.unimib.triviaducks.repository.question;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.util.List;

public interface QuestionResponseCallback {
    void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
}
