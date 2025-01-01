package com.unimib.triviaducks.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> getUserPreferences(String idToken);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
    void saveUserPreferences(String username, String idToken);
    void saveUserImage(String imageName, String idToken);
}
