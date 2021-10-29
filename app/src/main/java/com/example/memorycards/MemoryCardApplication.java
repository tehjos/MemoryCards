package com.example.memorycards;

import android.app.Application;

import timber.log.Timber;

public class MemoryCardApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
