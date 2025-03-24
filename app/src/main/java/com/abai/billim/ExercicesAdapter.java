package com.abai.billim;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ExercicesAdapter extends RecyclerView.Adapter<ExercicesAdapter.ExercisesViewHolder> {
    private Context context;
    private List<ExercisesShortItem> items;
    private Map<String, Integer> completed;
    private boolean[] isHide;
    private float defaultHeight, defaultHeightItem;
    private int chosenQuestion;
    private ValueAnimator animatorShow, animatorShowItem, animatorHide, animatorHideItem;

    private OnExerciseClickListener onExerciseClickListener;

    public interface OnExerciseClickListener {
        void OnExerciseClick(int position);
    }

    public void setOnExerciseClickListener(OnExerciseClickListener onExerciseClickListener) {
        this.onExerciseClickListener = onExerciseClickListener;
    }

    public ExercicesAdapter(Context context, List<ExercisesShortItem> items, Map<String, Integer> completed, int chosenQuestion) {
        this.context = context;
        this.items = items;
        this.completed = completed;
        this.isHide = new boolean[items.size()];
        this.chosenQuestion = chosenQuestion;
        defaultHeight = context.getResources().getDimension(R.dimen.dp1) * 88;
        defaultHeightItem = context.getResources().getDimension(R.dimen.dp1) * 112;
    }

    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_exercise, parent, false);
        return new ExercisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (items.get(position).getLessonNumber().isEmpty()) {
            holder.exerciseId.setText("Кастомное задание");
        } else {
            holder.exerciseId.setText((position + 1) + " сабақ");
        }
        holder.exerciseTheme.setText(items.get(position).getTheme());
        holder.exerciseCompletedOf.setText(String.valueOf(items.get(position).getAmount()));
        int completedCount = 0;
        try {
            if (completed.get(items.get(position).id) != null) completedCount = completed.get(items.get(position).id);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            completedCount = 0;
        }
        holder.exerciseCompleted.setText(String.valueOf(completedCount));
        holder.itemView.setOnClickListener(v -> {
            if (isHide[position]) {
                animateView(holder, defaultHeight, 0);
                animateViewItem(holder, defaultHeightItem + defaultHeight, defaultHeightItem);
                holder.goToExplanation.setOnClickListener(null);
            } else {
                animateView(holder, 0, defaultHeight);
                animateViewItem(holder, defaultHeightItem, defaultHeightItem + defaultHeight);
                holder.goToExplanation.setOnClickListener(v1 -> {
                    if (onExerciseClickListener != null) {
                        onExerciseClickListener.OnExerciseClick(position);
                    }
                });
            }
            isHide[position] = !isHide[position];
        });
        if (chosenQuestion != -1) {
            if (chosenQuestion == holder.getAdapterPosition()) {
                animateView(holder, 0, defaultHeight);
                animateViewItem(holder, defaultHeightItem, defaultHeightItem + defaultHeight);
                chosenQuestion = -1;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void animateView(ExercisesViewHolder holder, float from, float to) {
        if (animatorShow != null && animatorShow.isRunning()) {
            animatorShow.cancel();
        }
        if (animatorHide != null && animatorHide.isRunning()) {
            animatorHide.cancel();
        }

        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            holder.movingLayout.setTranslationY(value);
        });

        if (to > from) {
            animatorShow = animator;
            animatorShow.start();
        } else {
            animatorHide = animator;
            animatorHide.start();
        }
    }

    private void animateViewItem(ExercisesViewHolder holder, float from, float to) {
        if (animatorShowItem != null && animatorShowItem.isRunning()) {
            animatorShowItem.cancel();
        }
        if (animatorHideItem != null && animatorHideItem.isRunning()) {
            animatorHideItem.cancel();
        }

        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            layoutParams.height = (int) value;
            holder.itemView.setLayoutParams(layoutParams);
        });

        if (to > from) {
            animatorShowItem = animator;
            animatorShowItem.start();
        } else {
            animatorHideItem = animator;
            animatorHideItem.start();
        }
    }


    public class ExercisesViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseId, exerciseTheme, exerciseCompleted, exerciseCompletedOf;
        Button goToExplanation;
        FrameLayout movingLayout;

        public ExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseId = itemView.findViewById(R.id.exerciseId);
            exerciseTheme = itemView.findViewById(R.id.exerciseTheme);
            exerciseCompleted = itemView.findViewById(R.id.exerciseCompleted);
            exerciseCompletedOf = itemView.findViewById(R.id.exerciseCompletedOf);
            movingLayout = itemView.findViewById(R.id.movingLayout);
            goToExplanation = itemView.findViewById(R.id.goToExplanation);
        }
    }
}
