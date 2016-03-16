package com.chrymsler.mytodolist;

import android.app.Application
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject;

public class ThisApplication extends Application {
    static ThisApplication singleton
    ArrayList<String> titles
    ArrayList<ArrayList<String>> todos

    public static ThisApplication getInstance () {
        return singleton
    }

    @Override
    void onCreate() {
        super.onCreate()

        singleton = this

        String jsondata

        // load data from file
        File file = new File(getFilesDir(), "appdata")
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file)
            byte[] bytes = new byte[file.length()]
            try {
                inputStream.read(bytes)
            } finally {
                inputStream.close()
            }

            jsondata = new String(bytes)
        } else {
            jsondata = '{"main":[{"title":"Tutorial", "todos":[ {"todo":"This is a todo entry."}, {"todo":"Swipe me left/right to delete."}, {"todo":"Drag up/down to reposition."}, {"todo":"Tap to edit."}]}]}'
        }

        // loading mock values
        titles = []
        todos = [[]]

        JSONObject jsonObject = new JSONObject(jsondata)
        JSONArray jsonArray = jsonObject.getJSONArray("main")

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i)
            titles[i] = obj.getString("title")

            todos[i] = []
            JSONArray jsonArray1 = obj.getJSONArray("todos")
            for (int j = 0; j < jsonArray1.length(); j++) {
                JSONObject obj1 = jsonArray1.getJSONObject(j)
                todos.get(i).putAt(j, obj1.getString("todo"))
            }
        }
    }

    void saveToFile () {
        JSONArray arr1 = new JSONArray()
        for (int i = 0; i < titles.size(); i++) {
            JSONArray arr2 = new JSONArray()
            for (int j = 0; j < todos[i].size(); j++) {
                JSONObject obj = new JSONObject()
                obj.put("todo", todos[i][j])
                arr2.put(obj)
            }

            JSONObject obj2 = new JSONObject()
            obj2.put("title", titles[i])
            obj2.put("todos", arr2)

            arr1.put(obj2)
        }

        JSONObject jsonData = new JSONObject()
        jsonData.put("main", arr1)

        FileOutputStream outputStream = openFileOutput("appdata", Context.MODE_PRIVATE)
        try {
            outputStream.write(jsonData.toString().bytes)
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }
}
