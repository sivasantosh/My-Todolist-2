package com.chrymsler.mytodolist;

import android.app.Application;

public class ThisApplication extends Application {
    static ThisApplication singleton
    String[] titles
    def todos

    public static def getInstance () {
        return singleton
    }

    @Override
    void onCreate() {
        super.onCreate()

        singleton = this

        // loading mock values
        titles = ["testy 1", "testy 2", "testy 3"]
        todos = [[]]

        titles.eachWithIndex { String entry, int i ->
            todos[i] = []
            (0..3).each {
                todos[i][it] = titles[i] + ": " + it
            }
        }
    }
}
