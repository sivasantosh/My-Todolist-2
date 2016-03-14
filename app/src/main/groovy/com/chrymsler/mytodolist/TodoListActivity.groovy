package com.chrymsler.mytodolist

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TodoListActivity extends AppCompatActivity {
    RecyclerView mTodolistView
    TodoListAdapter mAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent()
        int index = intent.getIntExtra("index", 0)
        println "titles size is "+ThisApplication.instance.titles.size()
        setTitle("\""+ThisApplication.instance.titles[index]+"\" todos")

        // load recycler view of todolist
        mTodolistView = (RecyclerView) findViewById(R.id.todolistRecyclerView)

        // set layout manager for recycler view
        mTodolistView.setLayoutManager(new LinearLayoutManager(this))

        // create and link adapter with recycler view
        mAdapter = new TodoListAdapter(index)
        mTodolistView.setAdapter(mAdapter)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
