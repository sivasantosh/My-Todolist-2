package com.chrymsler.mytodolist;

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater;
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView;
import android.widget.TextView
import groovy.transform.CompileStatic;

@CompileStatic
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
        String p = ThisApplication.instance.getPriority(index, position).toString()
        int d = mTodoListActivity.getResources().getIdentifier(p, "drawable", mTodoListActivity.getPackageName())
        holder.imageView.setImageResource(d)

        String item = ThisApplication.instance.getTodo(index, position)
        holder.todoItem.setText(item)

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                int pos = ThisApplication.instance.getTodosIndex(index, item)
                switch (ThisApplication.instance.getPriority(index, pos)) {
                    case ThisApplication.Priority.normal:
                        ThisApplication.instance.setPriority(index, pos, ThisApplication.Priority.completed)
                        break
                    case ThisApplication.Priority.completed:
                        ThisApplication.instance.setPriority(index, pos, ThisApplication.Priority.important)
                        break
                    case ThisApplication.Priority.important:
                        ThisApplication.instance.setPriority(index, pos, ThisApplication.Priority.normal)
                        break
                }

                notifyItemChanged(pos)
            }
        })

        holder.todoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                // edit this item
                int pos = ThisApplication.instance.getTodosIndex(index, item)
                mTodoListActivity.editTodoItem(pos, item)
            }
        })
    }

    @Override
    int getItemCount() {
        ThisApplication.instance.getTodosCount(index)
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoItem
        ImageView imageView

        ViewHolder (View v) {
            super(v);

            todoItem = (TextView) v.findViewById(R.id.todoItemTextView)
            imageView = (ImageView) v.findViewById(R.id.imageView2)
        }
    }

    TodoListAdapter (int i, TodoListActivity activity) {
        mTodoListActivity = activity
        index = i
    }
}
