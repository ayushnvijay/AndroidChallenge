package com.example.ayush.androidchallenge.DataManagementUtils;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by ayush on 1/15/18.
 */
@Dao
public interface SuggestionDao {
    @Query("SELECT * FROM " + Suggestion.TABLE_NAME)
    List<Suggestion> getAllSuggestions();

    @Insert
    void insertSuggestions(Suggestion... suggestions);

    @Delete
    void deleteSuggestion(Suggestion suggestion);
}
