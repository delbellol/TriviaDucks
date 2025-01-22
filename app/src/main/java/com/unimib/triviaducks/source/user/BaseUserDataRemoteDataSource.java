package com.unimib.triviaducks.source.user;

import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.repository.user.UserResponseCallback;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);

    public abstract void getUserPreferences(String idToken);

    public abstract void saveUserPreferences(String username, String idToken);

    public abstract void saveUserImage(String imageName, String idToken);
}
