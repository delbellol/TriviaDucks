package com.unimib.triviaducks.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.unimib.triviaducks.util.Constants;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question implements Parcelable {
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

    public Question() {

    }

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

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + question + '\'' +
                ", correctAnswer='" + correctAnswer +
                "}";
    }

    /*
     * Used to fill the shimmer list
     */
    //TODO
    public static Question getSampleQuestion() {
        Question sample = new Question();
        sample.setQuestion("SAMPLE");
        sample.setCorrectAnswer("Sample");
        List<String> incorrectAnswers = new ArrayList<>();
        incorrectAnswers.add("Sample");
        incorrectAnswers.add("Sample");
        incorrectAnswers.add("Sample");
        sample.setIncorrectAnswers(incorrectAnswers);
        return sample;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(this.uid);
        parcel.writeString(this.type);
        parcel.writeString(this.difficulty);
        parcel.writeString(this.category);
        parcel.writeString(this.question);
        parcel.writeString(this.correctAnswer);
        parcel.writeStringList(this.incorrectAnswers);
    }

    public void readFromParcel(Parcel source) {
        this.uid = source.readLong();
        this.type = source.readString();
        this.difficulty = source.readString();
        this.category = source.readString();
        this.question = source.readString();
        this.correctAnswer = source.readString();
        this.incorrectAnswers = source.createStringArrayList();
    }

    protected Question(Parcel in) {
        this.uid = in.readLong();
        this.type = in.readString();
        this.difficulty = in.readString();
        this.category = in.readString();
        this.question = in.readString();
        this.correctAnswer = in.readString();
        this.incorrectAnswers = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}