package com.unimib.triviaducks.source;

import static com.unimib.triviaducks.util.Constants.UNEXPECTED_ERROR;

import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.JSONParserUtils;

import java.io.IOException;

public class QuestionMockDataSource extends BaseQuestionRemoteDataSource {
    private final JSONParserUtils jsonParserUtil;

    public QuestionMockDataSource(JSONParserUtils jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getQuestions() {
        QuestionAPIResponse questionAPIResponse = null;

        try {
            questionAPIResponse = jsonParserUtil.parseJSONFileWithGSon(Constants.SAMPLE_JSON_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (questionAPIResponse != null) {
            questionCallback.onSuccessFromRemote(questionAPIResponse, System.currentTimeMillis());
        } else {
            questionCallback.onFailureFromRemote(new Exception(UNEXPECTED_ERROR));
        }
    }
}
