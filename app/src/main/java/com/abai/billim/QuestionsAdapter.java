package com.abai.billim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {

    private Context context;
    private List<String> item;
    private List<List<String>> items;
    private OnAnswerClickListener onAnswerClickListener;

    public QuestionsAdapter(Context context, List<String> item, List<List<String>> items, OnAnswerClickListener onAnswerClickListener) {
        this.context = context;
        this.item = item;
        this.items = items;
        this.onAnswerClickListener = onAnswerClickListener;
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionsViewHolder(LayoutInflater.from(context).inflate(R.layout.view_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position) {
        holder.question.setText(item.get(position));

        AnswerAdapter adapter = new AnswerAdapter(context, items.get(position), position, onAnswerClickListener);
        holder.RV.setLayoutManager(new LinearLayoutManager(context));
        holder.RV.setAdapter(adapter);
        holder.RV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class QuestionsViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        RecyclerView RV;
        public QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            RV = itemView.findViewById(R.id.answers);
        }
    }
}
