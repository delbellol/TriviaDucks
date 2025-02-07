package com.unimib.triviaducks.ui.welcome.viewmodel;

import static com.unimib.triviaducks.util.Constants.ERROR_ID_TOKEN_NULL;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.repository.user.IUserRepository;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<Result> userPreferencesMutableLiveData;
    private MutableLiveData<Result> usernameMutableLiveData;
    private MutableLiveData<Result> leaderboardMutableLiveData;
    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        if (userMutableLiveData == null) {
            getUserData(token);
        }
        return userMutableLiveData;
    }

    public void saveUserUsername(String username, String idToken) {
        if (idToken != null) {
            userRepository.saveUserUsername(username, idToken);
        }
    }

    public void saveUserImage(String imageName, String idToken) {
        if (idToken != null) {
            userRepository.saveUserImage(imageName, idToken);
        }
    }

    public void saveBestScore(int score, String idToken) {
        if (idToken != null) {
            userRepository.saveBestScore(score, idToken);
        }
    }

    public void updateCategoryCounter(String category, String idToken) {
        if (idToken != null) {
            userRepository.updateCategoryCounter(category, idToken);
        }
    }

    public MutableLiveData<Result> getUserUsername(String idToken) {
        if (idToken != null) {
            usernameMutableLiveData = userRepository.getUserUsername(idToken);
        }
        return usernameMutableLiveData;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }
        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }

    public MutableLiveData<Result> getLeaderboard() {
        leaderboardMutableLiveData = userRepository.getLeaderboard();
        return leaderboardMutableLiveData;
    }
    public MutableLiveData<Result> fetchUserInformations(String idToken) {
        if (idToken != null) {
            userPreferencesMutableLiveData = userRepository.fetchUserInformations(idToken);
        }
        else Log.e(TAG,ERROR_ID_TOKEN_NULL);

        return getUserInformations();
    }

    public MutableLiveData<Result> getUserInformations() {
        return userPreferencesMutableLiveData;
    }
}
