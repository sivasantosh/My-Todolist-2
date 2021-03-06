package com.chrymsler.mytodolist

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu;
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import groovy.transform.CompileStatic;

@CompileStatic
public class TodoListActivity extends AppCompatActivity {
    RecyclerView mTodolistView
    TextView mTodosHelpText
    TodoListAdapter mAdapter
    int mIndex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent()
        mIndex = intent.getIntExtra("index", 0)
        setTitle("\""+ThisApplication.instance.getTitle(mIndex)+"\" todos")

        // load recycler view of todolist
        mTodolistView = (RecyclerView) findViewById(R.id.todolistRecyclerView)

        mTodosHelpText = (TextView) findViewById(R.id.todosHelpText)

        configureTodosVisibility()

        // set layout manager for recycler view
        mTodolistView.setLayoutManager(new LinearLayoutManager(this))

        // create and link adapter with recycler view
        mAdapter = new TodoListAdapter(mIndex, this)
        mTodolistView.setAdapter(mAdapter)

        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                )
            }

            @Override
            boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition()
                int toPos = target.getAdapterPosition()

                if (fromPos < toPos) {
                    for (int i = fromPos; i < toPos; i++) {
                        ThisApplication.instance.swapTodos(mIndex, i, i+1)
                    }
                } else {
                    for (int i = fromPos; i > toPos ; i--) {
                        ThisApplication.instance.swapTodos(mIndex, i, i-1)
                    }
                }

                mAdapter.notifyItemMoved(fromPos, toPos)

                return true
            }

            @Override
            void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition()

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                if (sp.getBoolean("confirmTodoDelete", true)) {
                    confirmDeleteTodo(pos)
                } else {
                    deleteTodo(pos)
                }
            }
        })

        ith.attachToRecyclerView(mTodolistView)
    }

    void deleteTodo (int pos) {
        ThisApplication.instance.removeTodo(mIndex, pos)
        mAdapter.notifyItemRemoved(pos)

        configureTodosVisibility()
    }

    void confirmDeleteTodo (int pos) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                setMessage("Are you sure?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        deleteTodo(pos)
                    }
                }).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemChanged(pos)
                    }
                })
        dialog.show()
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
            case R.id.new_todo:
                // get to do from user using a dialog
                EditText input = new EditText(this)

                AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                    setTitle("New Todo").
                    setView(input).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        void onClick(DialogInterface dialog, int which) {
                            def i = ThisApplication.instance.addTodo(mIndex, input.getText().toString())

                            configureTodosVisibility()

                            mAdapter.notifyItemInserted(i)
                            mTodolistView.scrollToPosition(i)
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
            case R.id.action_settings1:
                startActivity(new Intent(this, MyPreferencesActivity.class))
                return true
            case R.id.action_about1:
                startActivity(new Intent(this, AboutActivity.class))
                return true
        }

        return super.onOptionsItemSelected(item);
    }

    void configureTodosVisibility () {
        if (ThisApplication.instance.getTodosCount(mIndex) > 0) {
            mTodolistView.visibility = View.VISIBLE
            mTodosHelpText.visibility = View.GONE
        } else {
            mTodolistView.visibility = View.GONE
            mTodosHelpText.visibility = View.VISIBLE
        }
    }

    void editTodoItem (int index, String todo) {
        EditText input = new EditText(this)
        input.setText(todo)

        AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                setTitle("Edit Todo").
                setView(input).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        ThisApplication.instance.setTodo(mIndex, index, input.getText().toString())
                        mAdapter.notifyItemChanged(index)
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        dialog.cancel()
                    }
                })
        dialog.show()
    }

    @Override
    protected void onStop() {
        new SaveToFile().execute("test")

        super.onStop()
    }
}
