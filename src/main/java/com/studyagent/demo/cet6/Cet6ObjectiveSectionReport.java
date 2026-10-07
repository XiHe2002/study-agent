package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6ObjectiveSectionReport {

    private String section;
    private String answerSource;

    private int total;
    private int correct;
    private int wrong;
    private double accuracy;

    private List<Cet6PreviewResponse> wrongQuestions;

    public Cet6ObjectiveSectionReport(
            String section,
            String answerSource,
            int total,
            int correct,
            int wrong,
            double accuracy,
            List<Cet6PreviewResponse> wrongQuestions
    ) {
        this.section = section;
        this.answerSource = answerSource;
        this.total = total;
        this.correct = correct;
        this.wrong = wrong;
        this.accuracy = accuracy;
        this.wrongQuestions = wrongQuestions;
    }

    public String getSection() {
        return section;
    }

    public String getAnswerSource() {
        return answerSource;
    }

    public int getTotal() {
        return total;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public List<Cet6PreviewResponse> getWrongQuestions() {
        return wrongQuestions;
    }
}