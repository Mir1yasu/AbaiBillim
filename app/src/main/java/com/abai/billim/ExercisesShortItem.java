package com.abai.billim;

import java.io.Serializable;

public class ExercisesShortItem implements Serializable {
    String lessonNumber, id, theme;
    int amount;

    public ExercisesShortItem(String lessonNumber, String id, String theme, int amount) {
        this.lessonNumber = lessonNumber;
        this.id = id;
        this.theme = theme;
        this.amount = amount;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getAmount() {
        return amount;
    }
}