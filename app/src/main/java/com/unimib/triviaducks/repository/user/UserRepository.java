package com.unimib.triviaducks.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.QuestionAPIResponse;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.repository.question.QuestionResponseCallback;
import com.unimib.triviaducks.source.question.BaseQuestionLocalDataSource;
import com.unimib.triviaducks.source.user.BaseUserAuthenticationRemoteDataSource;
import com.unimib.triviaducks.source.user.BaseUserDataRemoteDataSource;

import java.util.List;

/**
 * Repository class to get the user information.
 */
public class UserRepository implements IUserRepository, UserResponseCallback, QuestionResponseCallback {
    private static final String TAG = UserRepository.class.getSimpleName();
    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseQuestionLocalDataSource questionLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BaseQuestionLocalDataSource questionLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.questionLocalDataSource = questionLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.questionLocalDataSource.setQuestionCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }
    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserPreferences(String idToken) {
        userDataRemoteDataSource.getUserPreferences(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void saveUserPreferences(String username, String idToken) {
        userDataRemoteDataSource.saveUserPreferences(username, idToken);
    }

    @Override
    public void saveUserImage(String imageName, String idToken) {
        userDataRemoteDataSource.saveUserImage(imageName, idToken);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserSuccess result = new Result.UserSuccess(user);
        userMutableLiveData.postValue(result);
    }
    @Override
    public void onSuccessFromRemoteDatabase(List<Question> questionList) {
        questionLocalDataSource.insertQuestions(questionList);
    }

    @Override
    public void onSuccessFromGettingUserPreferences() {
        userPreferencesMutableLiveData.postValue(new Result.UserSuccess(null));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
    }

    @Override
    public void onSuccessFromRemote(QuestionAPIResponse questionAPIResponse, long lastUpdate) {
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
    }

    @Override
    public void onSuccessFromLocal(List<Question> questionList) {
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
    }

    //@Override
    public void onSuccessFromCloudReading(List<Question> questionList) {
    }

    //@Override
    public void onSuccessFromCloudWriting(Question question) {
    }

    //@Override
    public void onFailureFromCloud(Exception exception) {
    }
}
