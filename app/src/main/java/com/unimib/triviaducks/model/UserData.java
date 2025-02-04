package com.unimib.triviaducks.model;

import android.os.Parcelable;

import androidx.room.Entity;

@Entity
public class UserData {
    private String username;
    private String image;

    public UserData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
