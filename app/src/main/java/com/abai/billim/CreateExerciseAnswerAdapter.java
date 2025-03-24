package com.abai.billim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExerciseAnswerAdapter extends RecyclerView.Adapter<CreateExerciseAnswerAdapter.CreateExerciseAnswerViewHolder> {

    private final Context context;
    private int size;
    private int correctAnswer = 0;
    private boolean isSecond = false, isKkInFocus = true;
    private final boolean[] isAnimationPlaying = {false};
    private boolean[] playAnimation;
    private Map<Integer, String> itemsKk = new HashMap<>();
    private Map<Integer, String> itemsRu = new HashMap<>();
    private List<String> correctAnswerKk = new ArrayList<>();
    private List<String> correctAnswerRu = new ArrayList<>();

    public CreateExerciseAnswerAdapter(Context context) {
        this.context = context;
        size = 3;
        for (int i = 0; i < size; i++) {
            itemsKk.put(i, "");
            itemsRu.put(i, "");
            correctAnswerKk.add(null);
            correctAnswerRu.add(null);
        }
    }

    @NonNull
    @Override
    public CreateExerciseAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreateExerciseAnswerViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_answer_creating_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreateExerciseAnswerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!isKkInFocus) {
            holder.questionCreatingAnswerRu.requestFocus();
            holder.questionCreatingAnswerRu.setFocusable(true);
            holder.questionCreatingAnswerRu.setFocusableInTouchMode(true);
        } else {
            holder.questionCreatingAnswerRu.setFocusable(false);
            holder.questionCreatingAnswerRu.setFocusableInTouchMode(false);
        }
        if (position == (size - 1) && size < 7) {
            holder.questionCreatingAdd.setVisibility(View.VISIBLE);
            holder.questionCreatingAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemsKk.put(size, "");
                    itemsRu.put(size, "");
                    correctAnswerKk.add(null);
                    correctAnswerRu.add(null);
                    size++;
                    playAnimation = new boolean[size];
                    notifyItemInserted(size);
                    notifyItemChanged(0);
                    notifyItemChanged(size - 2);
                }
            });
        } else {
            holder.questionCreatingAdd.setVisibility(View.GONE);
        }
        if (size == 1) {
            itemsKk.put(size, "");
            itemsRu.put(size, "");
            correctAnswerKk.add(null);
            correctAnswerRu.add(null);
            size++;
            playAnimation = new boolean[size];
            notifyItemInserted(size);
            notifyItemChanged(0);
            notifyItemChanged(size - 2);
        }
        if (position == 0 && size > 2) {
            holder.questionCreatingRemove.setVisibility(View.VISIBLE);
            holder.questionCreatingRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    size--;
                    itemsKk.remove(size);
                    itemsRu.remove(size);
                    correctAnswerKk.remove(size);
                    correctAnswerRu.remove(size);
                    playAnimation = new boolean[size];
                    notifyItemRemoved(size);
                    notifyItemChanged(size - 1);
                    notifyItemChanged(0);
                }
            });
        } else {
            holder.questionCreatingRemove.setVisibility(View.GONE);
        }
        holder.isCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctAnswerKk.get(position) == null) {
                    holder.isCorrect.setAlpha(1f);
                    correctAnswerKk.set(position, itemsKk.get(position));
                    correctAnswerRu.set(position, itemsRu.get(position));
                } else {
                    holder.isCorrect.setAlpha(0.45f);
                    correctAnswerKk.set(position, null);
                    correctAnswerRu.set(position, null);
                }
//                try {
//                    if (correctAnswer < size - 1) {
//                        if (correctAnswerKk.get(position) == null) {
//                            holder.isCorrect.setAlpha(1f);
//                            correctAnswerKk.set(position, itemsKk.get(position));
//                            correctAnswerRu.set(position, itemsRu.get(position));
//                            correctAnswer++;
//                        } else {
//                            holder.isCorrect.setAlpha(0.45f);
//                            correctAnswerKk.set(position, null);
//                            correctAnswerRu.set(position, null);
//                            correctAnswer--;
//                        }
//                    } else {
//                        if (correctAnswerKk.get(position) == null) {
//                            Toast.makeText(context, "Оставьте как минимум один неверный ответ!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            holder.isCorrect.setAlpha(0.45f);
//                            correctAnswerKk.set(position, null);
//                            correctAnswerRu.set(position, null);
//                            correctAnswer--;
//                        }
//                    }
//                } catch (IndexOutOfBoundsException | NullPointerException e) {
//                    if (correctAnswer < size - 1) {
//                        holder.isCorrect.setAlpha(1f);
//                        correctAnswerKk.set(position, itemsKk.get(position));
//                        correctAnswerRu.set(position, itemsRu.get(position));
//                        correctAnswer++;
//                    } else {
//                        Toast.makeText(context, "Оставьте как минимум один неверный ответ!", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });
        if (playAnimation == null) {
            playAnimation = new boolean[size];
        }
        if (playAnimation[position]) {
            for (boolean b : playAnimation) {
                if (b) {
                    isAnimationPlaying[0] = false;
                    break;
                }
            }
            ((Main) context).startAnimation(isKkInFocus ? holder.questionCreatingAnswerRu : holder.questionCreatingAnswerKk, isKkInFocus ? holder.questionCreatingAnswerKk : holder.questionCreatingAnswerRu, isAnimationPlaying);
            playAnimation[position] = false;
        }
        if (isSecond) {
            holder.questionCreatingAnswerRu.setVisibility(View.VISIBLE);
            if (holder.questionCreatingAnswerRu.getTag() == null) {
                holder.questionCreatingAnswerRu.addTextChangedListener(new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int lineCount = holder.questionCreatingAnswerRu.getLineCount();
                        int lineHeight = holder.questionCreatingAnswerRu.getLineHeight();
                        int desiredHeight = lineCount * lineHeight;

                        holder.questionCreatingAnswerRu.setHeight(desiredHeight);
                        itemsRu.put(holder.getAdapterPosition(), holder.questionCreatingAnswerRu.getText().toString());
                    }
                });
            }
        } else {
            holder.questionCreatingAnswerRu.setVisibility(View.GONE);
            holder.questionCreatingAnswerRu.setFocusable(false);
            holder.questionCreatingAnswerRu.setFocusableInTouchMode(false);
            if (!isKkInFocus) ((Main) context).startAnimation(holder.questionCreatingAnswerKk, holder.questionCreatingAnswerRu, isAnimationPlaying);
        }
        if (holder.questionCreatingAnswerKk.getTag() == null) {
            holder.questionCreatingAnswerKk.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int lineCount = holder.questionCreatingAnswerKk.getLineCount();
                    int lineHeight = holder.questionCreatingAnswerKk.getLineHeight();
                    int desiredHeight = lineCount * lineHeight;
                    holder.questionCreatingAnswerKk.setHeight(desiredHeight);
                    itemsKk.put(holder.getAdapterPosition(), holder.questionCreatingAnswerKk.getText().toString());
                }
            });
        }
        holder.questionCreatingAnswerKk.setText(itemsKk.get(position));
        holder.questionCreatingAnswerRu.setText(itemsRu.get(position));
        System.out.println(correctAnswerKk.get(position));
        holder.isCorrect.setAlpha(correctAnswerKk.get(position) == null || correctAnswerKk.get(position).replaceAll(" ", "").isEmpty() ? 0.45f : 1f);
    }
    public void setAnimation(boolean isKkInFocus, boolean isSecond) {
        playAnimation = new boolean[size];
        Arrays.fill(playAnimation, true);
        this.isSecond = isSecond;
        this.isKkInFocus = isKkInFocus;
    }
    @Override
    public int getItemCount() {
        return size;
    }
    public Map<Integer, String> getItemsKk() {
        return itemsKk;
    }
    public Map<Integer, String> getItemsRu() {
        return itemsRu;
    }

    public List<String> getCorrectAnswerKk() {
        return correctAnswerKk;
    }

    public List<String> getCorrectAnswerRu() {
        return correctAnswerRu;
    }

    public void setItems(Map<Integer, String> itemsKk, Map<Integer, String> itemsRu, List<String> correctAnswerKk, List<String> correctAnswerRu, int size) {
        this.itemsKk.clear();
        this.itemsRu.clear();
        this.correctAnswerKk.clear();
        this.correctAnswerRu.clear();
        for (int i = 0; i < itemsKk.size(); i++) {
            this.itemsKk.put(i, itemsKk.get(i));
            this.itemsRu.put(i, itemsRu.get(i));
        }
        if (correctAnswerKk.size() != 0) {
            for (int i = 0; i < correctAnswerKk.size(); i++) {
                this.correctAnswerKk.add(correctAnswerKk.get(i));
                this.correctAnswerRu.add(correctAnswerRu.get(i));
            }
        } else {
            for (int i = 0; i < size; i++) {
                this.correctAnswerKk.add(null);
                this.correctAnswerRu.add(null);
            }
        }
        System.out.println(correctAnswerKk.size());
        this.size = size;
        playAnimation = new boolean[size];
        for (int i = 0; i < correctAnswerKk.size(); i++) {
            if (correctAnswerKk.get(i) != null && !correctAnswerKk.get(i).replaceAll(" ", "").equals("")) this.correctAnswer++;
        }
        this.correctAnswer = correctAnswerKk.size();
    }

    public void setSecond(boolean second) {
        isSecond = second;
        notifyDataSetChanged();
    }
    public class CreateExerciseAnswerViewHolder extends RecyclerView.ViewHolder {
        EditText questionCreatingAnswerKk, questionCreatingAnswerRu;
        Button questionCreatingAdd, questionCreatingRemove, isCorrect;
        public CreateExerciseAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            questionCreatingAnswerKk = itemView.findViewById(R.id.questionCreatingAnswerKk);
            questionCreatingAnswerRu = itemView.findViewById(R.id.questionCreatingAnswerRu);
            questionCreatingAdd = itemView.findViewById(R.id.questionCreatingAdd);
            questionCreatingRemove = itemView.findViewById(R.id.questionCreatingRemove);
            isCorrect = itemView.findViewById(R.id.isCorrect);
        }
    }
    public abstract class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
