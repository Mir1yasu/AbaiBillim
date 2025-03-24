package com.abai.billim;
//toremove
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;/*
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;*/

import java.util.ArrayList;
import java.util.List;

public class CreateExerciseQuestionAdapter /*extends RecyclerView.Adapter<CreateExerciseQuestionAdapter.CreateExerciseQuestionViewHolder>*/ {
    /*

    private Context context;
    private int size;
    private boolean isSecond = false, playAnimation = false, isKkInFocus = true, error = false;
    private boolean[] isAnimationPlaying = {false};
    private CreateExerciseAnswerAdapter adapter;
    private List<String> itemsKk = new ArrayList<>();
    private List<String> itemsRu = new ArrayList<>();
    private List<List<String>> itemsChildKk = new ArrayList<>();
    private List<List<String>> itemsChildRu = new ArrayList<>();
    public CreateExerciseQuestionAdapter(Context context) {
        this.context = context;
        size = 1;
        for (int i = 0; i < size; i++) {
            itemsKk.add("");
            itemsRu.add("");
            itemsChildKk.add(new ArrayList<>());
            itemsChildRu.add(new ArrayList<>());
        }
    }

    @NonNull
    @Override
    public CreateExerciseQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreateExerciseQuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_question_creating_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreateExerciseQuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        adapter = new CreateExerciseAnswerAdapter(context);
        holder.RV.setLayoutManager(new LinearLayoutManager(context));
        holder.RV.setAdapter(adapter);
        if (playAnimation) {
            ((Main) context).startAnimation(isKkInFocus ? holder.questionRu : holder.questionKk, isKkInFocus ? holder.questionKk : holder.questionRu, isAnimationPlaying);
            playAnimation = false;
        }
        if (error) {
            playAnimation = true;
            ((Main) context).startAnimation(holder.questionKk, holder.questionRu, isAnimationPlaying);
            playAnimation = false;
            error = false;
        }
        if (isSecond) {
            holder.questionRu.setVisibility(View.VISIBLE);
            initEditText(itemsRu, holder.questionRu, position);
            holder.questionRu.setFocusable(false);
            holder.questionRu.setFocusableInTouchMode(false);
            itemsChildRu.set(position, adapter.getItems());
        } else {
            holder.questionRu.setVisibility(View.GONE);
            holder.questionRu.setFocusable(false);
            holder.questionRu.setFocusableInTouchMode(false);
            itemsChildRu.set(position, adapter.getItems());
        }
        initEditText(itemsKk, holder.questionKk, position);
        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(Color.argb(55, 63, 0, 5));
        } else {
            holder.layout.setBackgroundColor(Color.argb(0, 255, 255, 255));
        }
        itemsChildKk.set(position, adapter.getItems());
    }

    @Override
    public int getItemCount() {
        return size;
    }
    public void setItemCount() {
        size++;
        itemsKk.add("");
        itemsRu.add("");
        itemsChildKk.add(new ArrayList<>());
        itemsChildRu.add(new ArrayList<>());
    }
    private void initEditText (List<String> items, EditText editText, int position) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lineCount = editText.getLineCount();
                int lineHeight = editText.getLineHeight();
                int desiredHeight = lineCount * lineHeight;

                editText.setHeight(desiredHeight);
                items.set(position, editText.getText().toString());
            }
        });
    }
    public void setAnimation(boolean kkInFocus, boolean isSecond) {
        playAnimation = true;
        this.isKkInFocus = kkInFocus;
        this.isSecond = isSecond;
        if (!isSecond && !isKkInFocus) {
            error = true;
        }
        adapter.setAnimation(this.isKkInFocus, this.isSecond);
        adapter.notifyDataSetChanged();
    }

    //    private void startAnimation(EditText show, EditText hide) {
//        isAnimationPlaying = true;
//        ObjectAnimator animShowX, animShowY, animShowAlpha, animGoneX, animGoneY, animGoneAlpha;
//        animShowX = ObjectAnimator.ofFloat(show, "x", show.getX(), hide.getX());
//        animShowY = ObjectAnimator.ofFloat(show, "y", show.getY(), hide.getY());
//        animShowAlpha = ObjectAnimator.ofFloat(show, View.ALPHA, 0.45f, 1f);
//
//        animGoneX = ObjectAnimator.ofFloat(hide, "x", hide.getX(), show.getX());
//        animGoneY = ObjectAnimator.ofFloat(hide, "y", hide.getY(), show.getY());
//        animGoneAlpha = ObjectAnimator.ofFloat(hide, "alpha", 1f, 0.45f);
//
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.playTogether(animShowX, animShowY, animShowAlpha, animGoneX, animGoneY, animGoneAlpha);
//        animSet.setInterpolator(new DecelerateInterpolator());
//        animSet.setDuration(500);
//        animSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(@NonNull Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(@NonNull Animator animator) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isAnimationPlaying = false;
//                    }
//                },150);
//            }
//
//            @Override
//            public void onAnimationCancel(@NonNull Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(@NonNull Animator animator) {
//
//            }
//        });
//        show.setElevation(5f);
//        show.setFocusable(true);
//        show.setFocusableInTouchMode(true);
//        hide.setElevation(1f);
//        hide.setFocusable(false);
//        hide.setFocusableInTouchMode(false);
//        animSet.start();
//    }
    public List<List<String>> getItemsChildKk() {
        for (int i = 0; i < itemsChildKk.size(); i++) {
            for (int j = 0; j < itemsChildKk.get(i).size(); j++) {
                if (itemsChildKk.get(i).get(j) == null || itemsChildKk.get(i).get(j).replaceAll(" ", "").equals("")) return null;
            }
        }
        return itemsChildKk;
    }
    public List<List<String>> getItemsChildRu() {
        for (int i = 0; i < itemsChildRu.size(); i++) {
            for (int j = 0; j < itemsChildRu.get(i).size(); j++) {
                if (itemsChildRu.get(i).get(j) == null || itemsChildRu.get(i).get(j).replaceAll(" ", "").equals("")) return null;
            }
        }
        return itemsChildRu;
    }
    public List<String> getItemsKk() {
        for (int i = 0; i < itemsKk.size(); i++) {
            if (itemsKk.get(i) == null || itemsKk.get(i).replaceAll(" ", "").equals("")) return null;
        }
        return itemsKk;
    }
    public List<String> getItemsRu() {
        for (int i = 0; i < itemsRu.size(); i++) {
            if (itemsRu.get(i) == null || itemsRu.get(i).replaceAll(" ", "").equals("")) return null;
        }
        return itemsRu;
    }
    static class CreateExerciseQuestionViewHolder extends RecyclerView.ViewHolder {

        EditText questionKk, questionRu;
        RecyclerView RV;
        LinearLayout layout;
        public CreateExerciseQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            questionKk = itemView.findViewById(R.id.questionCreatingQuestionKk);
            questionRu = itemView.findViewById(R.id.questionCreatingQuestionRu);
            RV = itemView.findViewById(R.id.questionAnswer);

        }
    }
*/
}
