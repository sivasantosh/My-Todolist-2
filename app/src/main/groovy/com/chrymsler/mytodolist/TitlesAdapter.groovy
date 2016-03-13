package com.chrymsler.mytodolist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup;

public class TitlesAdapter extends RecyclerView.Adapter<ViewHolder> {
    String[] mDataSet
    MainActivity mainActivity;

    @Override
    ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        def v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.title_item, viewGroup, false)
        return new ViewHolder(v)
    }

    @Override
    void onBindViewHolder(ViewHolder viewHolder, int i) {
        def name = mDataSet[i]
        viewHolder.titleTextView.setText(name)

        viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                mainActivity.gotoTodoListActivity(name)
            }
        })
    }

    @Override
    int getItemCount() {
        return mDataSet.size()
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        def titleTextView;

        ViewHolder (View v) {
            super(v);

            titleTextView = v.findViewById(R.id.titleTextView)
        }
    }

    TitlesAdapter (MainActivity activity) {
        mainActivity = activity
        mDataSet = ThisApplication.instance.titles
    }

    void addTodoList(String title) {
        mDataSet += title
    }
}
