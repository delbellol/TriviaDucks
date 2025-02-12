package com.unimib.triviaducks.source.user;

import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.repository.user.UserResponseCallback;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);

    public abstract void getUserUsername(String idToken);

    public abstract void getUserImages(String idToken);

    public abstract void getUserBestScore(String idToken);

    public abstract void getCategoriesPodium(String idToken);

    public abstract void getLeaderboard();

    public abstract void saveUserUsername(String username, String idToken);

    public abstract void saveUserImage(String imageName, String idToken);

    public abstract void saveBestScore(int score, String idToken);

    public abstract void updateCategoryCounter(String category, String idToken);

    public abstract void fetchUserInformations(String idToken);
}
