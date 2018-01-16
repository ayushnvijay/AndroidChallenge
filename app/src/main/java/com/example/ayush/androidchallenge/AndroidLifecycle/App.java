package com.example.ayush.androidchallenge.AndroidLifecycle;

import android.app.Application;

import com.example.ayush.androidchallenge.AppUtils.AppModule;
import com.example.ayush.androidchallenge.Components.AppComponent;
import com.example.ayush.androidchallenge.Components.DaggerAppComponent;
import com.example.ayush.androidchallenge.DataManagementUtils.DatabaseModule;
import com.example.ayush.androidchallenge.NetworkUtils.NetworkModule;

public class App extends Application {
    private static AppComponent component;
    public static AppComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        component = buildComponent();
    }

    private AppComponent buildComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .databaseModule(new DatabaseModule())
                .build();
    }
}
