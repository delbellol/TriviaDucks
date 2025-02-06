package com.unimib.triviaducks.model;

public class Rank {
    private String profileImage;
    private String name;
    private int score;

    public Rank(String profileImage, String name, int score) {
        this.profileImage = profileImage;
        this.name = name;
        this.score = score;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
