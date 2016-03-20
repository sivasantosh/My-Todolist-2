package com.chrymsler.mytodolist

import android.os.AsyncTask;

public class SaveToFile extends AsyncTask<Object, Void, Void> {
    @Override
    protected Void doInBackground(Object... params) {
        ThisApplication.instance.saveToFile()

        return null
    }
}
