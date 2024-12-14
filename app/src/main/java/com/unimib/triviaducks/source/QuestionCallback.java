package com.unimib.triviaducks.source;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;

import java.util.List;

public interface QuestionCallback {
    void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Question> questionList);
    void onFailureFromLocal(Exception exception);
}
