package com.example.ayush.androidchallenge.DataManagementUtils;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ayush on 1/15/18.
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    AppDatabase providesDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "sugg_db")
                .build();
    }
}
