package com.example.ayush.androidchallenge.DataManagementUtils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by ayush on 1/15/18.
 */
@Database(entities = Suggestion.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SuggestionDao geSuggestionDao();
}
