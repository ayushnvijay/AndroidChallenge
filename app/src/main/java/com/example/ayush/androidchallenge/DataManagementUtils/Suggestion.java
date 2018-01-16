package com.example.ayush.androidchallenge.DataManagementUtils;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ayush on 1/15/18.
 */
@Entity(tableName = "suggestion")
public class Suggestion {
    public static final String TABLE_NAME = "suggestion";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "suggestion_string")
    private String suggestion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
