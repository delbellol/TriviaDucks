package com.unimib.triviaducks.util;

import com.unimib.triviaducks.model.Question;

import java.util.List;

public interface ResponseCallback {
    void onSuccess(List<Question> questionList, long lastUpdate);
    void onFailure(String errorMessage);
}
