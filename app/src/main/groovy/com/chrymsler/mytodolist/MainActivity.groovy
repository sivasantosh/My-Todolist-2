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
                        ThisApplication.instance.swapTodoList(i, i+1)
                    }
                } else {
                    for (int i = fromPos; i > toPos; i--) {
                        ThisApplication.instance.swapTodoList(i, i-1)
                    }
                }

                mTitlesAdapter.notifyItemMoved(fromPos, toPos)

                return true
            }

            @Override
            void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // delete the respective title and todolist
                int pos = viewHolder.adapterPosition
                ThisApplication.instance.removeTodoList(pos)

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                        setTitle("New Folder").
                        setView(input).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            void onClick(DialogInterface dialog, int which) {
                                def i = ThisApplication.instance.addTodoList(input.getText().toString())
                                gotoTodoListActivity(i)
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
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class))
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
        input.setText(ThisApplication.instance.getTitle(index))
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).
                setTitle("Rename Folder").
                setView(input).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    void onClick(DialogInterface dialog, int which) {
                        ThisApplication.instance.setTitle(index, input.getText().toString())
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
