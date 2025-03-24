package com.abai.billim;

import java.io.Serializable;
import java.util.List;

public class ExercisesItem implements Serializable {
    String id;
    String theme, exercisesType;
    List<ExplanationStringItem> explanation;
    List<QuestionItem> exercise;

    public ExercisesItem(String id, String theme, String exercisesType, List<ExplanationStringItem> explanation, List<QuestionItem> exercise) {
        this.id = id;
        this.theme = theme;
        this.exercisesType = exercisesType;
        this.explanation = explanation;
        this.exercise = exercise;
    }
}
