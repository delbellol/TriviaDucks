package com.unimib.triviaducks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @PrimaryKey
    public long uid;
    private String type;
    private String difficulty;
    private String category;
    private String question;
    @JsonProperty("correct_answer")
    @SerializedName("correct_answer")
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;

    // Getters e setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public static Question getSampleQuestion() {
        Question sample = new Question();
        sample.setQuestion("Not so long question sample");
        sample.setCorrectAnswer("Not so long correct answer sample");
        List<String> incorrectAnswers = new ArrayList<>();
        incorrectAnswers.add("Not so long incorrect answer sample");
        incorrectAnswers.add("Not so long incorrect answer sample");
        incorrectAnswers.add("Not so long incorrect answer sample");
        sample.setIncorrectAnswers(incorrectAnswers);
        return sample;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + question + "   "   + correctAnswer ;
    }
}
