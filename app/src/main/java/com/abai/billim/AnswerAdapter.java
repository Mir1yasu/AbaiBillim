package com.abai.billim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private Context context;
    private List<String> items;
    private int parentPosition;
    private OnAnswerClickListener onAnswerClickListener;
    private String[] symbolOfAnswer = {"A) ", "B)", "C)", "D)", "E)", "F)"};
    private int lastClickedPosition = RecyclerView.NO_POSITION; // Индекс последнего нажатого элемента, изначально устанавливаем NO_POSITION

    public AnswerAdapter(Context context, List<String> items, int parentPosition, OnAnswerClickListener onAnswerClickListener) {
        this.context = context;
        this.items = items;
        this.parentPosition = parentPosition;
        this.onAnswerClickListener = onAnswerClickListener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnswerViewHolder(LayoutInflater.from(context).inflate(R.layout.view_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.answer.setText(symbolOfAnswer[position] + " " + items.get(position));

        // Устанавливаем альфа-значение в зависимости от того, был ли элемент недавно нажат
        holder.itemView.setAlpha(lastClickedPosition == position ? 0.8f : 1.0f);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClickedPosition = holder.getAdapterPosition(); // Устанавливаем текущий индекс нажатого элемента
                notifyDataSetChanged();
                onAnswerClickListener.onAnswerClick(parentPosition, position, items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answer;
        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
