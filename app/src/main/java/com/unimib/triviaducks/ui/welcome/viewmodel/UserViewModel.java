package com.unimib.triviaducks.ui.welcome.viewmodel;

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
    private MutableLiveData<Result> userImagesMutableLiveData;
    private MutableLiveData<Result> userPodiumMutableLiveData;
    private MutableLiveData<Result> userBestScoreMutableLiveData;
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

    public MutableLiveData<Result> getUserInformations() {
        return userPreferencesMutableLiveData;
    }
    public MutableLiveData<Result> fetchUserInformations(String idToken) {
        if (idToken != null) {
            userPreferencesMutableLiveData = userRepository.fetchUserInformations(idToken);
        }
        return getUserUsername();
    }

    public MutableLiveData<Result> fetchUserUsername(String idToken) {
        if (idToken != null) {
            userPreferencesMutableLiveData = userRepository.fetchUserUsername(idToken);
        }
        return getUserUsername();
    }

    public MutableLiveData<Result> getUserUsername() {
        return userPreferencesMutableLiveData;
    }

    public MutableLiveData<Result> getUserImages(String idToken) {
        if (idToken != null) {
            userImagesMutableLiveData = userRepository.getUserImages(idToken);
        }
        Log.d(TAG, userImagesMutableLiveData.toString());
        return userImagesMutableLiveData;
    }

    public MutableLiveData<Result> getUserBestScore(String idToken) {
        if (idToken != null) {
            userBestScoreMutableLiveData = userRepository.getUserBestScore(idToken);
        }
        Log.d(TAG, userBestScoreMutableLiveData.toString());
        return userBestScoreMutableLiveData;
    }

    public MutableLiveData<Result> getCategoriesPodium(String idToken) {
        if (idToken != null) {
            userPodiumMutableLiveData = userRepository.getCategoriesPodium(idToken);
        }
        return userPodiumMutableLiveData;
    }


//    //TODO cambiare nome
//    public MutableLiveData<Result> fetchUserDBData(String idToken) {
//        // Chiamata al repository per ottenere le domande e aggiornare questionListLiveData
//        if (questionListLiveData == null) {
//            questionListLiveData = questionRepository.fetchQuestions(category,questionAmount, difficulty);
//        }
//        return getUserDBData();
//    }
//
    //TODO Cambiare nome
//    public MutableLiveData<Result> getUserDBData() {
//        return questionListLiveData;
//    }

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
}
