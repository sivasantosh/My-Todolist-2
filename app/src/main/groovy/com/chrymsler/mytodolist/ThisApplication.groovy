package com.chrymsler.mytodolist;

import android.app.Application;

public class ThisApplication extends Application {
    static ThisApplication singleton
    String[] titles

    public static def getInstance () {
        return singleton
    }

    @Override
    void onCreate() {
        super.onCreate()

        singleton = this

        titles = ["testy 1", "testy 2", "testy 3"]
    }
}
