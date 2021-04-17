package com.cleanup.todoc;

import android.app.Application;

import com.cleanup.todoc.utils.DependencyContainer;

public class TodocApplication extends Application {
    public DependencyContainer dependencyContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        dependencyContainer = new DependencyContainer(this);
    }
}
