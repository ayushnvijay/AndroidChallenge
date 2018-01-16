package com.example.ayush.androidchallenge.Components;

import com.example.ayush.androidchallenge.AndroidLifecycle.MainActivity;
import com.example.ayush.androidchallenge.AndroidLifecycle.MediaFragment;
import com.example.ayush.androidchallenge.AndroidLifecycle.SearchFragment;
import com.example.ayush.androidchallenge.AppUtils.AppModule;
import com.example.ayush.androidchallenge.AppUtils.ImageAdapter;
import com.example.ayush.androidchallenge.DataManagementUtils.DatabaseModule;
import com.example.ayush.androidchallenge.NetworkUtils.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

// Component consists of these modules. Component is pretty much a combined module of these many modules.
@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class})
@Singleton
public interface AppComponent {
    void inject(SearchFragment searchFragment);
    void inject(MediaFragment mediaFragment);
    void inject(ImageAdapter adapter);
    void inject(MainActivity mainActivity);
}
