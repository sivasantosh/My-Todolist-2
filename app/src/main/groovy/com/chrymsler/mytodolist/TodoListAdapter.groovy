package com.chrymsler.mytodolist;

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater;
import android.view.View
import android.view.ViewGroup;
import android.widget.TextView;

public class TodoListAdapter extends RecyclerView.Adapter<ViewHolder> {
    TodoListActivity mTodoListActivity
    int index;

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        def v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item, parent, false)
        return new ViewHolder(v)
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        String item = ThisApplication.instance.todos[index][position]
        holder.todoItem.setText(item)

        holder.todoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                // edit this item
                mTodoListActivity.editTodoItem(position, item)
            }
        })
    }

    @Override
    int getItemCount() {
        return ThisApplication.instance.todos[index].size()
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoItem

        ViewHolder (View v) {
            super(v);

            todoItem = (TextView) v.findViewById(R.id.todoItemTextView)
        }
    }

    TodoListAdapter (int i, TodoListActivity activity) {
        mTodoListActivity = activity
        index = i
    }
}
