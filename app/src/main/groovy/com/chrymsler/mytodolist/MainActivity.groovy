package com.chrymsler.mytodolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import groovy.transform.CompileStatic;

@CompileStatic
public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TitlesAdapter mTitlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main)

        mRecyclerView = (RecyclerView) findViewById(R.id.titles)

        mRecyclerView.setHasFixedSize(true)

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this))

        mTitlesAdapter = new TitlesAdapter(this)
        mRecyclerView.setAdapter(mTitlesAdapter)

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
                int fromPos = viewHolder.adapterPosition
                int toPos = target.adapterPosition

                if (fromPos < toPos) {
                    for (int i = fromPos; i < toPos; i++) {
                        println "< "+i
                        def tmp = ThisApplication.instance.titles[i]
                        ThisApplication.instance.titles[i] = ThisApplication.instance.titles[i+1]
                        ThisApplication.instance.titles[i+1] = tmp
                    }
                } else {
                    for (int i = fromPos; i > toPos; i--) {
                        println "> "+i
                        def tmp = ThisApplication.instance.titles[i]
                        ThisApplication.instance.titles[i] = ThisApplication.instance.titles[i-1]
                        ThisApplication.instance.titles[i-1] = tmp
                    }
                }

                println "exchanging "+fromPos+" to "+toPos

                def tmp1 = ThisApplication.instance.todos[fromPos]
                ThisApplication.instance.todos[fromPos] = ThisApplication.instance.todos[toPos]
                ThisApplication.instance.todos[toPos] = tmp1

                println "comparing from "+ThisApplication.instance.titles[fromPos]+" "+ThisApplication.instance.todos[fromPos][0]
                println "comparing to "+ThisApplication.instance.titles[toPos]+" "+ThisApplication.instance.todos[toPos][0]

                mTitlesAdapter.notifyItemMoved(fromPos, toPos)

                return true
            }

            @Override
            void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // delete the respective title and todolist
                int pos = viewHolder.adapterPosition
                ThisApplication.instance.todos.removeAt(pos)
                ThisApplication.instance.titles.removeAt(pos)

                mTitlesAdapter.notifyItemRemoved(pos)
            }
        })

        ith.attachToRecyclerView(mRecyclerView)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.new_list:
                EditText input = new EditText(this)
                def that = this;
                AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                        setTitle("New TodoList").
                        setView(input).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            void onClick(DialogInterface dialog, int which) {
                                gotoTodoListActivity(mTitlesAdapter.addTodoList(input.getText().toString()))
                            }
                        }).
                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            void onClick(DialogInterface dialog, int which) {
                                dialog.cancel()
                            }
                        })

                dialog.show()
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void gotoTodoListActivity (int index) {
        Intent intent = new Intent(this, TodoListActivity.class)
        intent.putExtra("index", index)

        startActivity(intent)
    }

    void editTitle (int index) {
        EditText input = new EditText(this)
        input.setText(ThisApplication.instance.titles[index])
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                setTitle("Edit Title").
                setView(input).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        ThisApplication.instance.titles[index] = input.getText().toString()
                        mTitlesAdapter.notifyItemChanged(index)
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

    class SaveToFile extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            ThisApplication.instance.saveToFile()

            return null
        }
    }

    @Override
    protected void onStop() {
        new SaveToFile().execute("test")

        super.onStop()
    }
}
