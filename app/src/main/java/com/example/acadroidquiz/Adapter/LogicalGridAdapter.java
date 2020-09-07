package com.example.acadroidquiz.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acadroidquiz.QuestionsActivity;
import com.example.acadroidquiz.R;

import java.util.List;

public class LogicalGridAdapter extends BaseAdapter {

    List<String> sets;
    String category;

    public LogicalGridAdapter(List<String> sets, String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item,parent,false);
        }
        else
        {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuestionsActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("setId", sets.get(position));
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView)view.findViewById(R.id.textview)).setText(String.valueOf(position+1));

        return view;
    }
}
