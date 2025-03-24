package com.abai.billim;

import java.io.Serializable;
import java.util.List;

public class CompletedItem implements Serializable {
    String id;
    List<String> correctAt;
    List<String> mistakeAt;
    int completed;
    int answerAmount;

    public CompletedItem(String id, List<String> correctAt, List<String> mistakeAt, int completed, int answerAmount) {
        this.id = id;
        this.correctAt = correctAt;
        this.mistakeAt = mistakeAt;
        this.completed = completed;
        this.answerAmount = answerAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCorrectAt() {
        return correctAt;
    }

    public void setCorrectAt(List<String> correctAt) {
        this.correctAt = correctAt;
    }

    public List<String> getMistakeAt() {
        return mistakeAt;
    }

    public void setMistakeAt(List<String> mistakeAt) {
        this.mistakeAt = mistakeAt;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getAnswerAmount() {
        return answerAmount;
    }

    public void setAnswerAmount(int answerAmount) {
        this.answerAmount = answerAmount;
    }
}
