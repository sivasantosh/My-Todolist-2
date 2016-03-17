package com.chrymsler.mytodolist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic;

@CompileStatic
public class TitlesAdapter extends RecyclerView.Adapter<ViewHolder> {
    MainActivity mainActivity;

    @Override
    ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        def v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.title_item, viewGroup, false)
        return new ViewHolder(v)
    }

    @Override
    void onBindViewHolder(ViewHolder viewHolder, int i) {
        def name = ThisApplication.instance.getTitle(i)
        viewHolder.titleTextView.setText(name)

        viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                int pos = ThisApplication.instance.getTitlesIndex(name)
                mainActivity.gotoTodoListActivity(pos)
            }
        })

        viewHolder.titleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            boolean onLongClick(View v) {
                int pos = ThisApplication.instance.getTitlesIndex(name)
                mainActivity.editTitle(pos)
                return true
            }
        })
    }

    @Override
    int getItemCount() {
        return ThisApplication.instance.todoListsCount
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        ViewHolder (View v) {
            super(v);

            titleTextView = (TextView) v.findViewById(R.id.titleTextView)
        }
    }

    TitlesAdapter (MainActivity activity) {
        mainActivity = activity
    }
}
