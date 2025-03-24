package com.abai.billim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends Fragment implements com.abai.billim.OnAnswerClickListener {
    private ExercisesItem item;
    private TextView type;
    private LinearLayout questions;
    private TextView id, theme;
    private RecyclerView RV;
    private QuestionsAdapter adapter;
    private Button finish;
    private ExerciseReader jsonReader;
    private List<String> chosenAnswer = new ArrayList<>();
    private List<List<TextView>> answer = new ArrayList<>();
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_questions, container, false);
        type = root.findViewById(R.id.type);
        //questions = root.findViewById(R.id.questions);
        finish = root.findViewById(R.id.finish);
        RV = root.findViewById(R.id.RV);
        id = root.findViewById(R.id.id);
        theme = root.findViewById(R.id.theme);
        jsonReader = new ExerciseReader(getContext(), ((Main) getContext()).getChosenId() + ".json", ((Main) getContext()).getLanguage());
        item = jsonReader.readJson();
        if (item == null) {
            item = jsonReader.readJsonCustom();
        }
        System.out.println(item.id);
        id.setText(((Main) getContext()).getPosition() + 1 + " сабақ");
        type.setText(item.exercisesType);
        List<List<String>> temp = new ArrayList<>();
        List<String> temp1 = new ArrayList<>();
        for (int i = 0; i < item.exercise.size(); i++) {
            chosenAnswer.add(null);
            List<String> temp0 = new ArrayList<>(item.exercise.get(i).answer);
            temp.add(temp0);
            temp1.add(item.exercise.get(i).question);
        }
        theme.setText(item.theme);
        adapter = new QuestionsAdapter(getContext(), temp1, temp, this);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        RV.setAdapter(adapter);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNull = false;
                for (int i = 0; i < chosenAnswer.size(); i++) {
                    if (null == chosenAnswer.get(i)) isNull = true;
                }
                if (!isNull) {
                    List<CompletedItem> temp;
                    List<String> tempCorrect = new ArrayList<>();
                    List<String> tempWrong = new ArrayList<>();
                    List<String> id;
                    int completed = 0;
                    for (int i = 0; i < item.exercise.size(); i++) {
                        for (int j = 0; j < item.exercise.get(i).correctAnswers.size(); j++) {
                            if (item.exercise.get(i).correctAnswers.get(j).equals(chosenAnswer.get(i))) {
                                completed++;
                                tempCorrect.add(chosenAnswer.get(i));
                            } else {
                                tempWrong.add(chosenAnswer.get(i));
                            }
                        }
                    }
                    temp = ((Main) getContext()).getUser().getItems();
                    id = ((Main) getContext()).getUser().id;
                    for (int i = 0; i < id.size(); i++) {
                        if (id.get(i).equals(item.id)) {
                            ((Main) getContext()).getUser().id.remove(i);
                            ((Main) getContext()).getUser().items.remove(i);
                        }
                    }
                    id.add(item.id);
                    System.out.println(item.id);
                    temp.add(new CompletedItem(item.id, tempCorrect, tempWrong, completed, item.exercise.size()));
                    ((Main) getContext()).getUser().setItems(temp, id);
                    ((Main) getContext()).saveUser();
                    ((Main) getContext()).setAnsweredQuestion();
                    ((Main) getContext()).replaceFragment(new ExercisesFragment());
                }
            }
        });
        return root;
    }


    @Override
    public void onAnswerClick(int parentPosition, int childPosition, String answer) {
        chosenAnswer.set(parentPosition, answer);
    }
}
