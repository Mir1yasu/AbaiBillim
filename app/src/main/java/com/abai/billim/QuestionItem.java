package com.abai.billim;

import java.io.Serializable;
import java.util.List;

public class QuestionItem implements Serializable {
    String question;
    List<String> answer;
    List<String> correctAnswers;

    public QuestionItem(String question, List<String> answer, List<String> correctAnswers) {
        this.question = question;
        this.answer = answer;
        this.correctAnswers = correctAnswers;
    }
}
