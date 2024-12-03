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

    @Query("SELECT * FROM Question WHERE liked = 1")
    List<Question> getLiked();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question... articles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Question> articles);

    @Update
    void updateArticle(Question article);

    @Delete
    void delete(Question user);

    @Query("DELETE from Question WHERE liked = 0")
    void deleteCached();

    @Query("DELETE from Question")
    void deleteAll();
}