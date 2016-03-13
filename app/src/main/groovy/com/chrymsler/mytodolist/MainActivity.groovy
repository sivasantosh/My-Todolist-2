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
import android.widget.EditText
import android.widget.Toast;

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
                                gotoTodoListActivity(input.getText().toString())
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

    void gotoTodoListActivity (String title) {
        mTitlesAdapter.addTodoList(title)

        Intent intent = new Intent(this, TodoListActivity.class)
        intent.putExtra("title", title)

        startActivity(intent)
    }
}
