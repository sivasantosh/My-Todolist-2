package com.chrymsler.mytodolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem
import android.widget.EditText;

public class TodoListActivity extends AppCompatActivity {
    RecyclerView mTodolistView
    TodoListAdapter mAdapter
    int mIndex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent()
        mIndex = intent.getIntExtra("index", 0)
        setTitle("\""+ThisApplication.instance.titles[mIndex]+"\" todos")

        // load recycler view of todolist
        mTodolistView = (RecyclerView) findViewById(R.id.todolistRecyclerView)

        // set layout manager for recycler view
        mTodolistView.setLayoutManager(new LinearLayoutManager(this))

        // create and link adapter with recycler view
        mAdapter = new TodoListAdapter(mIndex)
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
        switch (id) {
            case R.id.action_settings:
                return true
            case R.id.new_todo:
                // get to do from user using a dialog
                EditText input = new EditText(this)

                AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                    setTitle("New Todo").
                    setView(input).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        void onClick(DialogInterface dialog, int which) {
                            ThisApplication.instance.todos[mIndex] += input.getText().toString()
                            mAdapter.notifyItemInserted(ThisApplication.instance.todos[mIndex].size() - 1)
                        }
                    }).
                    setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        void onClick(DialogInterface dialog, int which) {
                            dialog.cancel()
                        }
                    })
                dialog.show()
                return true
        }

        return super.onOptionsItemSelected(item);
    }
}