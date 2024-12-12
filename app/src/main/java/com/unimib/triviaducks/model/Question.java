package com.unimib.triviaducks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.unimib.triviaducks.util.Constants;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @PrimaryKey(autoGenerate = true)
    private long uid;
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
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
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

    public static void filterQuestion(List<Question> questionList) {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getQuestion().equals(Constants.REMOVED_QUESTION)) {
                questionList.remove(i);
                i--;
            }
        }
    }

    public static Question getSampleQuestion() {
        Question sample = new Question();
        sample.setQuestion("What is the capital of France?");
        sample.setCorrectAnswer("Paris");
        List<String> incorrectAnswers = new ArrayList<>();
        incorrectAnswers.add("London");
        incorrectAnswers.add("Berlin");
        incorrectAnswers.add("Madrid");
        sample.setIncorrectAnswers(incorrectAnswers);
        return sample;
    }


    @NonNull
    @Override
    public String toString() {
        return "" + question + "   "   + correctAnswer ;
    }
}