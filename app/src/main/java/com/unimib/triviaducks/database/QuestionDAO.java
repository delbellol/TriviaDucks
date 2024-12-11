package com.unimib.triviaducks.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.unimib.triviaducks.model.Question;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question... questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Question> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertQuestionList(List<Question> questionList);

    @Update
    void updateQuestion(Question question);

    @Delete
    void delete(Question question);

    @Query("DELETE from Question")
    void deleteAll();
}