package com.chrymsler.mytodolist;

import android.app.Application
import groovy.transform.CompileStatic
import org.json.JSONArray
import org.json.JSONObject;

@CompileStatic
public class ThisApplication extends Application {
    enum Priority { normal, important, completed }

    class TodoEntry {
        String todo
        Priority priority

        TodoEntry (String t, Priority p) {
            todo = t
            priority = p
        }
    }

    private static ThisApplication singleton
    private List<String> titles
    private List<List<TodoEntry>> todos

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
            jsondata = '{"main":[{"title":"Tutorial", "todos":[ {"todo":"This is a todo entry.", "priority": "normal"}, {"todo":"Swipe left/right to delete.", "priority": "normal"}, {"todo":"Drag up/down to reposition.", "priority": "normal"}, {"todo":"Tap to edit.", "priority": "important"}]}]}'
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
                String todo = obj1.getString("todo")
                Priority priority1 = Priority.valueOf(obj1.getString("priority"))
                todos.get(i).putAt(j, new TodoEntry(todo, priority1))
            }
        }
    }

    void saveToFile () {
        JSONArray arr1 = new JSONArray()
        for (int i = 0; i < titles.size(); i++) {
            JSONArray arr2 = new JSONArray()
            for (int j = 0; j < todos[i].size(); j++) {
                JSONObject obj = new JSONObject()
                obj.put("todo", todos[i][j].todo)
                obj.put("priority", todos[i][j].priority)
                arr2.put(obj)
            }

            JSONObject obj2 = new JSONObject()
            obj2.put("title", titles[i])
            obj2.put("todos", arr2)

            arr1.put(obj2)
        }

        JSONObject jsonData = new JSONObject()
        jsonData.put("main", arr1)

        FileOutputStream outputStream = openFileOutput("appdata", MODE_PRIVATE)
        try {
            outputStream.write(jsonData.toString().bytes)
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }

    String getTitle (int list_id) {
        titles[list_id]
    }

    void setTitle (int list_id, String title) {
        titles[list_id] = title
    }

    int getTitlesIndex (String title) {
        titles.findIndexOf { it == title }
    }

    int getTodoListsCount () {
        return titles.size()
    }

    int addTodoList(String title) {
        titles += title
        int i = (titles.size() - 1)
        todos[i] = []

        return i
    }

    String getTodo (int list_id, int todo_id) {
        todos[list_id][todo_id].todo
    }

    int getTodosIndex (int list_id, String todo) {
        todos[list_id].findIndexOf { TodoEntry t ->
            t.todo == todo
        }
    }

    void setTodo (int list_id, int todo_id, String todo) {
        todos[list_id][todo_id].todo = todo
    }

    int addTodo (int list_id, String todo) {
        todos[list_id].add(new TodoEntry(todo, Priority.normal))
        todos[list_id].size() - 1
    }

    void swapTodoList (int from, int to) {
        def tmp = titles[from]
        titles[from] = titles[to]
        titles[to] = tmp

        def tmp1 = todos[from]
        todos[from] = todos[to]
        todos[to] = tmp1
    }

    void swapTodos (int list_id, int from, int to) {
        def tmp = todos[list_id][from]
        todos[list_id][from] = todos[list_id][to]
        todos[list_id][to] = tmp
    }

    void removeTodoList (int list_id) {
        todos.removeAt(list_id)
        titles.removeAt(list_id)
    }

    void removeTodo (int list_id, int todo_id) {
        todos[list_id].removeAt(todo_id)
    }

    int getTodosCount (int list_id) {
        todos[list_id].size()
    }
}
