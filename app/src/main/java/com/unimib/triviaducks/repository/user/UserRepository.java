package com.unimib.triviaducks.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.source.user.BaseUserAuthenticationRemoteDataSource;
import com.unimib.triviaducks.source.user.BaseUserDataRemoteDataSource;

import java.util.List;

/**
 * Repository class to get the user information.
 */
public class UserRepository implements IUserRepository, UserResponseCallback {
    private static final String TAG = UserRepository.class.getSimpleName();
    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
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
    public MutableLiveData<Result> getUserUsername() {
        userDataRemoteDataSource.getUserUsername();
        return userPreferencesMutableLiveData;
    }

    public MutableLiveData<Result> fetchUserInformations(String idToken) {
        userDataRemoteDataSource.fetchUserInformations(idToken);
        return getUserUsername(); // Restituisce il LiveData aggiornato
    }

    @Override
    public MutableLiveData<Result> fetchUserUsername(String idToken) {
        userDataRemoteDataSource.fetchUserInformations(idToken);
        return getUserUsername();
    }

    @Override
    public MutableLiveData<Result> getUserImages(String idToken) {
        userDataRemoteDataSource.getUserImages(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserBestScore(String idToken) {
        userDataRemoteDataSource.getUserBestScore(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getCategoriesPodium(String idToken) {
        userDataRemoteDataSource.getCategoriesPodium(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getLeaderboard() {
        userDataRemoteDataSource.getLeaderboard();
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
    public void saveUserUsername(String username, String idToken) {
        userDataRemoteDataSource.saveUserUsername(username, idToken);
    }

    @Override
    public void saveUserImage(String imageName, String idToken) {
        userDataRemoteDataSource.saveUserImage(imageName, idToken);
    }

    @Override
    public void saveBestScore(int score, String idToken) {
        userDataRemoteDataSource.saveBestScore(score, idToken);
    }

    @Override
    public void updateCategoryCounter(String category, String idToken) {
        userDataRemoteDataSource.updateCategoryCounter(category, idToken);
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
    public void onSuccessFromGettingUserPreferences() {
        userPreferencesMutableLiveData.postValue(new Result.UserDataSuccess(null));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
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
