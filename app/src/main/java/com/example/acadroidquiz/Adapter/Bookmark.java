package com.example.acadroidquiz.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acadroidquiz.Modal.QuestionM;
import com.example.acadroidquiz.R;

import java.util.List;

public class Bookmark extends RecyclerView.Adapter<Bookmark.viewHolder> {

    List<QuestionM> list;

    public Bookmark(List<QuestionM> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(list.get(position).getQuestionn(),list.get(position).getCorrectAnss(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView question, answer;
        ImageView clear;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.bm_question);
            answer = itemView.findViewById(R.id.bm_answer);
            clear = itemView.findViewById(R.id.bm_clear);
        }

        private void setData(String question, String answer, final int position)
        {
            this.question.setText(question);
            this.answer.setText(answer);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }
}
