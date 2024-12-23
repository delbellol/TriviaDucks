package com.unimib.triviaducks.model;

//TODO TEMPORANEO
public class Rank {
    private int position;
    private int profileImage;
    private String name;
    private int score;

    public Rank(int position, int profileImage, String name, int score) {
        this.position = position;
        this.profileImage = profileImage;
        this.name = name;
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
